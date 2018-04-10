package edu.cmu.adroitness.client.services.googlespeechrecognition.view;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatDialogFragment;

/**
 * A simple dialog with a message.
 *
 * <p>The calling {@link android.app.Activity} needs to implement {@link
 * MessageSpeechFragment.Listener}.</p>
 */
public class MessageSpeechFragment extends AppCompatDialogFragment {

    public interface Listener {
        /**
         * Called when the dialog is dismissed.
         */
        void onMessageDialogDismissed();
    }

    private static final String ARG_MESSAGE = "message";

    /**
     * Creates a new instance of {@link MessageSpeechFragment}.
     *
     * @param message The message to be shown on the dialog.
     * @return A newly created dialog fragment.
     */
    public static MessageSpeechFragment newInstance(String message) {
        final MessageSpeechFragment fragment = new MessageSpeechFragment();
        final Bundle args = new Bundle();
        args.putString(ARG_MESSAGE, message);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new AlertDialog.Builder(getContext())
                .setMessage(getArguments().getString(ARG_MESSAGE))
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((Listener) getActivity()).onMessageDialogDismissed();
                    }
                })
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        ((Listener) getActivity()).onMessageDialogDismissed();
                    }
                })
                .create();
    }
}
