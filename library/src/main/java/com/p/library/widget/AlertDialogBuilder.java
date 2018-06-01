package com.p.library.widget;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.TypedValue;
import android.widget.Button;
import android.widget.TextView;

import com.p.library.R;


/**
 * Created by Jiang on 2016/9/9.
 * <p>
 */
public class AlertDialogBuilder extends AlertDialog.Builder {

    public AlertDialogBuilder(Context context) {
        super(context);
    }

    public AlertDialogBuilder(Context context, int theme) {
        super(context, theme);
    }


    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();

        TextView title = (TextView) dialog.findViewById(android.support.v7.appcompat.R.id.alertTitle);
        if (title != null) {
            title.setTextSize(TypedValue.COMPLEX_UNIT_PX, title.getContext().getResources().getDimensionPixelSize(R.dimen.sp_17));
            title.setLineSpacing(0f, 1.2f);
//            title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL));
        }

        TextView message = (TextView) dialog.findViewById(android.R.id.message);
        if (message != null) {
            message.setTextSize(TypedValue.COMPLEX_UNIT_PX, message.getContext().getResources().getDimensionPixelSize(R.dimen.sp_16));
            message.setLineSpacing(0f, 1.2f);
//            message.setTextColor(ContextCompat.getColor(getContext(), R.color.text_5c5c5c));
        }

        int sp16 = getContext().getResources().getDimensionPixelSize(R.dimen.dp18);

        Button button = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        if (button != null) {
            button.setTextColor(ContextCompat.getColor(getContext(), R.color.text_color));
            button.setTextSize(TypedValue.COMPLEX_UNIT_PX, button.getContext().getResources().getDimensionPixelSize(R.dimen.sp_16));
            button.setPadding(sp16, sp16 / 2, sp16, sp16 / 2);
        }
        Button button1 = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        if (button1 != null) {
            button1.setTextSize(TypedValue.COMPLEX_UNIT_PX, button1.getContext().getResources().getDimensionPixelSize(R.dimen.sp_16));
            button1.setPadding(sp16, sp16 / 2, sp16, sp16 / 2);
        }

        return dialog;
    }
}
