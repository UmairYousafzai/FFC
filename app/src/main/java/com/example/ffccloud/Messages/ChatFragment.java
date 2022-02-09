package com.example.ffccloud.Messages;

import static com.example.ffccloud.utils.CONSTANTS.MESSAGE_NOTIFICATION_TITLE;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.ffccloud.Database.FfcDatabase;
import com.example.ffccloud.Login.GetUserInfoModel;
import com.example.ffccloud.MainActivity;
import com.example.ffccloud.Messages.Adapter.MessageRecyclerViewAdapter;
import com.example.ffccloud.model.Message;
import com.example.ffccloud.PushNotification.SendNoticationClass;
import com.example.ffccloud.databinding.FragmentChatBinding;
import com.example.ffccloud.utils.SharedPreferenceHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ChatFragment extends Fragment {

    private FragmentChatBinding mBinding;
    private String receiverID;
    private MessageRecyclerViewAdapter adapter;
    private String senderRoom, receiverRoom;
    private DatabaseReference databaseReference;
    private final List<Message> messageList = new ArrayList<>();
    private SendNoticationClass sendNoticationClass;
    private String senderID;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentChatBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();

        sendNoticationClass= new SendNoticationClass(requireContext());
        senderID= String.valueOf(SharedPreferenceHelper.getInstance(requireContext()).getUserID());
        assert getArguments() != null;
        String receiverUserName = ChatFragmentArgs.fromBundle(getArguments()).getRecevierUserName();
        ((MainActivity) requireActivity()).getSupportActionBar().setTitle(receiverUserName);

        receiverID = ChatFragmentArgs.fromBundle(getArguments()).getRecevierID();
        senderRoom = senderID + receiverID;
        receiverRoom = receiverID + senderID;

        databaseReference = FirebaseDatabase.getInstance().getReference();

        setUpRecyclerView();
        btnListener();
        getMessages();
    }

    private void setUpRecyclerView() {

        mBinding.chatMessageRecyclerview.setLayoutManager(new LinearLayoutManager(requireContext()));

        adapter = new MessageRecyclerViewAdapter(requireContext());
        mBinding.chatMessageRecyclerview.setAdapter(adapter);
    }

    private void getMessages() {

        databaseReference.child("chats")
                .child(senderRoom)
                .child("messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        messageList.clear();

                        for (DataSnapshot snapshot1 : snapshot.getChildren()) {

                            Message message;

                            message = snapshot1.getValue(Message.class);
                            assert message != null;
                            message.setMessageId(snapshot1.getKey());
                            messageList.add(message);

                        }
                        adapter.setMessageList(messageList);
                        mBinding.chatMessageRecyclerview.smoothScrollToPosition(messageList.size());

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

    }

    private void btnListener() {

        mBinding.btnSend.setOnClickListener(v -> {

            closeKeyboard();

            String messageString = mBinding.etMessage.getText().toString();
            mBinding.etMessage.setText("");

            if (messageString.length() > 0) {


                Date date = Calendar.getInstance().getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat(" hh:mm", Locale.getDefault());
                String formattedTime = dateFormat.format(date);

                Message message = new Message();

                message.setMessage(messageString);
                message.setSenderId(senderID);
                message.setTimestamp(date.getTime());
                message.setTime(formattedTime);
                databaseReference.child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(message).addOnSuccessListener(aVoid -> databaseReference.child("chats")
                                .child(receiverRoom)
                                .child("messages")
                                .push()
                                .setValue(message).addOnSuccessListener(aVoid1 -> sendNotification(message.getMessage())));

            } else {
                Toast.makeText(requireContext(), "Please type message.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendNotification(String  message1) {
        FfcDatabase ffcDatabase = FfcDatabase.getInstance(requireContext());
        GetUserInfoModel senderUser = ffcDatabase.dao().getLoginUser();
        String message = senderUser.getUserName()+ " : "+ message1;
        sendNoticationClass.UpdateToken();



        FirebaseDatabase.getInstance().getReference().child("Tokens").child(receiverID).child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String receiverUserToken = dataSnapshot.getValue(String.class);
                sendNoticationClass.sendNotifications(receiverUserToken, senderUser, MESSAGE_NOTIFICATION_TITLE, message, requireContext());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void closeKeyboard() {
        View view = requireActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}