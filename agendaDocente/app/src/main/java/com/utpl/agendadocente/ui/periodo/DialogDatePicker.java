package com.utpl.agendadocente.ui.periodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static String tipo;

    public static DialogDatePicker newInstance(String tipoPeriodo){
        tipo = tipoPeriodo;
        return new DialogDatePicker();
    }

    public interface DatePickerListener {
        void onDateSet(DatePicker datePicker, String fecha, String tipo);
    }

    private DatePickerListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DatePickerListener) getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(requireActivity().toString() + " must implements DatePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        int y = calendar.get(Calendar.YEAR);
        int m = calendar.get(Calendar.MONTH);
        int d = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(requireActivity(), this, y, m, d);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        SimpleDateFormat simpledateformat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar newDate = Calendar.getInstance();
        newDate.set(i, i1, i2);
        String selectedDate = simpledateformat.format(newDate.getTime());
        listener.onDateSet(datePicker, selectedDate, tipo);
    }
}
