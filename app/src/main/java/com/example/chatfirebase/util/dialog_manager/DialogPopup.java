package com.example.chatfirebase.util.dialog_manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.example.chatfirebase.R;

import java.util.Objects;

public class DialogPopup {

    private final Context context;
    private final LayoutInflater inflater;
    private DialogClickListener dialogClickListener;

    public void setOnDialogListener(DialogClickListener listener) {
        dialogClickListener = listener;
    }

    public DialogPopup(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void dialogAlert(Activity activity, String Title, String Message) {
        Dialog alertdialog = new Dialog(activity);
        alertdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertdialog.setContentView(R.layout.dialog_alert);
        alertdialog.setCancelable(false);

        TextView txtAlertTitle = (TextView) alertdialog.findViewById(R.id.text_title_alert_dialog);
        TextView txtAlertContent = (TextView) alertdialog.findViewById(R.id.text_content_alert_dialog);

        txtAlertTitle.setText(Title);
        txtAlertContent.setText(Message);

        Button btn_ok = (Button) alertdialog.findViewById(R.id.button_ok_alert_dialog);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogClickListener != null)
                    dialogClickListener.onDialogClicked(view.getId());

                if (alertdialog != null)
                    alertdialog.dismiss();
            }
        });
        alertdialog.show();
    }

    public void dialogChoice(Activity activity, String Title, String Message) {
        Dialog alertdialog = new Dialog(activity);
        alertdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Objects.requireNonNull(alertdialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertdialog.setContentView(R.layout.dialog_choice);
        alertdialog.setCancelable(false);

        TextView txtAlertTitle = (TextView) alertdialog.findViewById(R.id.text_title_choice_dialog);
        TextView txtAlertContent = (TextView) alertdialog.findViewById(R.id.text_content_choice_dialog);

        txtAlertTitle.setText(Title);
        txtAlertContent.setText(Message);

        Button btn_ok = (Button) alertdialog.findViewById(R.id.button_ok_choice_dialog);
        Button button_cancel = (Button) alertdialog.findViewById(R.id.button_cancel_choice_dialog);

        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogClickListener != null)
                    dialogClickListener.onDialogClicked(view.getId());

                if (alertdialog != null)
                    alertdialog.dismiss();
            }
        });

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dialogClickListener != null)
                    dialogClickListener.onDialogClicked(view.getId());

                if (alertdialog != null)
                    alertdialog.dismiss();
            }
        });
        alertdialog.show();
    }

}
