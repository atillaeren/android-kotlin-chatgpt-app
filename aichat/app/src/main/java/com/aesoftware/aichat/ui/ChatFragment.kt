package com.aesoftware.aichat.ui

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.aesoftware.aichat.R
import com.aesoftware.aichat.adapter.ChatRVAdapter
import com.aesoftware.aichat.database.RoomEntity
import com.aesoftware.aichat.viewmodel.ChatViewModel
import com.aesoftware.aichat.databinding.FragmentChatBinding
import com.aesoftware.aichat.utils.Constants
import com.revenuecat.purchases.Purchases
import com.revenuecat.purchases.getCustomerInfoWith
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {
    private lateinit var binding: FragmentChatBinding

    @Inject
    lateinit var chatRVAdapter: ChatRVAdapter

    private val messageList: MutableList<RoomEntity> = mutableListOf()
    private var lastMessage = ""

    private val viewModel: ChatViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {


            Log.d("premium", "${Constants.isStillPremium}")

            editTextListener()

            fillMessageList()

            createRecyclerView()

            copyMessage()

            tapToUnlock()

            settingsBtn.setOnClickListener {
                findNavController().navigate(R.id.action_chatFragment_to_settingsFragment)
            }

            sendBtn.setOnClickListener {
                sendMessage()
            }

            refreshBtn.setOnClickListener {
                sendLastMessage()
            }
        }

    }

    private fun editTextListener() {
        binding.apply {
            messageET.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                @SuppressLint("SuspiciousIndentation")
                override fun afterTextChanged(p0: Editable?) {
                    if (p0.isNullOrEmpty()) {
                        sendBtn.setImageResource(R.drawable.icon_send_unselected)
                    } else
                        sendBtn.setImageResource(R.drawable.icon_send_selected)
                }
            })
        }
    }

    private fun copyMessage() {
        binding.apply {
            chatRVAdapter.onCopyClick = { click ->
                copiedIV.visibility = View.VISIBLE

                // Get a reference to the system ClipboardManager
                val clipboardManager =
                    requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

                // Get the text to be copied
                val text = click.message

                // Set the text to be copied to the clipboard
                val clip = ClipData.newPlainText("text", text)
                clipboardManager.setPrimaryClip(clip)
                Handler(Looper.getMainLooper()).postDelayed({
                    copiedIV.visibility = View.GONE
                }, 1000)
            }
        }
    }

    private fun tapToUnlock() {
        binding.apply {
            chatRVAdapter.onUnlockClick = {
                Log.d("unlock", "unlock tıklandı")
                findNavController().navigate(R.id.action_chatFragment_to_inAppFragment)
            }
        }
    }

    private fun messageSending() {
        binding.apply {
            val message = messageET.text.toString().trim()
            lastMessage = message
            if (message.isNotEmpty()) {
                viewLifecycleOwner.lifecycleScope.launch {
                    viewModel.getResponse(message)
                }
                messageET.text.clear()
                scrollToBottom()
                chatRecycler.smoothScrollToPosition(messageList.size - 1)
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please enter your query..",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun sendMessage() {
        binding.apply {
            //getAllData()
            Purchases.sharedInstance.getCustomerInfoWith({ error -> /* Optional error handling */ }) { purchaserInfo ->
                if (purchaserInfo.entitlements["pro"]?.isActive == true) {
                    messageSending()
                } else {
                    if (messageList.size >= 7) {
                        messageET.setText("")
                        findNavController().navigate(R.id.action_chatFragment_to_trialEndFragment)

                    } else {
                        messageSending()
                    }
                }
            }
        }
    }

    private fun sendLastMessage() {
        //getAllData()
        Purchases.sharedInstance.getCustomerInfoWith({ error -> /* Optional error handling */ }) { purchaserInfo ->
            if (purchaserInfo.entitlements["pro"]?.isActive == true) {
                if (lastMessage == "") {
                    Toast.makeText(
                        requireContext(),
                        "You did not send any message..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.viewModelScope.launch {
                        viewModel.getResponse(lastMessage)
                    }
                }
            } else {
                if (lastMessage == "") {
                    Toast.makeText(
                        requireContext(),
                        "You did not send any message..",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    if (messageList.size >= 7) {
                        binding.messageET.setText("")
                        findNavController().navigate(R.id.action_chatFragment_to_trialEndFragment)

                    } else {
                        viewModel.viewModelScope.launch {
                            viewModel.getResponse(lastMessage)
                        }
                    }
                }
            }
        }
    }

    private fun fillMessageList() {
        viewModel.viewModelScope.launch {
            viewModel.allMessageList.collect { messages ->
                chatRVAdapter.messageList = messages.toMutableList() as ArrayList<RoomEntity>
                if (messages.isNotEmpty()) {
                    chatRVAdapter.notifyItemInserted(messages.size - 1)
                    scrollToBottom()
                    getAllData()
                    binding.chatRecycler.scrollToPosition(messages.size - 1)
                } else {
                    viewModel.botWelcomeMessage()
                }
            }
        }
    }

    private fun scrollToBottom() {
        binding.apply {
            chatRecycler.addOnLayoutChangeListener { _, _, _, _, bottom, _, _, _, oldBottom ->
                if (bottom < oldBottom) {
                    chatRecycler.smoothScrollToPosition(messageList.size)
                }
            }
        }
    }

    private fun createRecyclerView() {
        binding.apply {
            //getAllData()
            chatRVAdapter = ChatRVAdapter()
            val layoutManager = LinearLayoutManager(context)
            chatRecycler.layoutManager = layoutManager
            chatRecycler.adapter = chatRVAdapter
            chatRecycler.scrollToPosition(messageList.size - 1)
            scrollToBottom()
        }
    }

    private fun getAllData() {
        viewModel.viewModelScope.launch {
            viewModel.allMessageList.collect {
                messageList.clear()
                messageList.addAll(it)
                chatRVAdapter.notifyDataSetChanged()
                Log.d("messagelist", "${messageList.size}")
            }
        }
        scrollToBottom()
    }
}