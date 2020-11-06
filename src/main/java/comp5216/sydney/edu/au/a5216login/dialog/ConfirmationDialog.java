package comp5216.sydney.edu.au.a5216login.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import android.text.Html;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import comp5216.sydney.edu.au.a5216login.R;


public class ConfirmationDialog extends DialogFragment {
    private static final String TAG = ConfirmationDialog.class.getSimpleName();
    private static final String KEY_MESSAGE = "MESSAGE";
    private ConfirmationDialogListener listener;
    private String message;
    private int action;


    public interface ConfirmationDialogListener {
        void onPositiveButtonClicked(int action);

        void onNegativeButtonClicked(int action);
    }

    public static void show(AppCompatActivity activity, String message, int action) {
        ConfirmationDialog dialog = new ConfirmationDialog();
        dialog.message = message;
        dialog.action = action;
        dialog.show(activity.getSupportFragmentManager(), TAG);
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);
        listener = (ConfirmationDialogListener) ctx;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        super.onCreateDialog(savedInstanceState);

        if (savedInstanceState != null) {
            message = savedInstanceState.getString(KEY_MESSAGE);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(Html.fromHtml(message))
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onPositiveButtonClicked(action);
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onNegativeButtonClicked(action);
                    }
                });

        return builder.create();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_MESSAGE, message);
    }
}
