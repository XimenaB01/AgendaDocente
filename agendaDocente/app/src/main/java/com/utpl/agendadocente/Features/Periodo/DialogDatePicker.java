package com.utpl.agendadocente.Features.Periodo;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Objects;

public class DialogDatePicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private static String tipo;

    public static DialogDatePicker newInstance(String tipoPeriodo){
        tipo = tipoPeriodo;
        return new DialogDatePicker();
    }

    public interface DatePickerListener {
        void onDateSet(DatePicker datePicker, int year, int month, int day, String tipo);
    }

    private DatePickerListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (DatePickerListener) getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements DatePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(Objects.requireNonNull(getActivity()), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        listener.onDateSet(datePicker, i, i1, i2, tipo);
    }
}
