package com.utpl.agendadocente.Features.Horario;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class DialogTimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{

    private static String tipo;
    public static DialogTimePicker newInstance(String tipoHora){
        tipo = tipoHora;
        return new DialogTimePicker();
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        listener.onTimeSet(timePicker,i,i1, tipo);
    }

    public interface TimePickerListener {
        void onTimeSet(TimePicker timePicker, int hour, int minute, String tipo);
    }

    private TimePickerListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (TimePickerListener) getTargetFragment();
        }catch (Exception e){
            throw new ClassCastException(Objects.requireNonNull(getActivity()).toString() + " must implements TimePickerListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar calendar = Calendar.getInstance();
        int hora = calendar.get(Calendar.HOUR);
        int minutos = calendar.get(Calendar.MINUTE);
        return new TimePickerDialog(getActivity(),this,hora,minutos, DateFormat.is24HourFormat(getContext()));
    }
}
