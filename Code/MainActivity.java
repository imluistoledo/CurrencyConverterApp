package com.example.currencyconverter;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    final String[] datos = new String[] {"Dolares", "Euros", "Pesos"};

    private Spinner spMonedaActual;
    private Spinner spMonedaCambio;
    private EditText etValorCambiar;
    private TextView tvResultado;

    final private double factorDolarEuro = 0.84;
    final private double factorPesoDolar = 0.05;
    final private double factorPesoEuro = 0.042;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adaptador = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, datos);

        spMonedaActual = (Spinner) findViewById(R.id.spMonedaActual);
        spMonedaActual.setAdapter(adaptador);

        spMonedaCambio = (Spinner) findViewById(R.id.spMonedaCambio);
        spMonedaCambio.setAdapter(adaptador);

        SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);

        String tmpMonedaActual = preferencias.getString("monedaActual", "");
        String tmpMonedaCambio = preferencias.getString("monedaCambio", "");

        if (!tmpMonedaActual.equals("")) {
            int indice = adaptador.getPosition(tmpMonedaActual);
            spMonedaActual.setSelection(indice);
        }
        if (tmpMonedaCambio.equals("")) {
            int indice = adaptador.getPosition(tmpMonedaCambio);
            spMonedaCambio.setSelection(indice);
        }
    }

    @SuppressLint("DefaultLocale")
    public void clickConvertir(View v) {
        spMonedaActual = (Spinner) findViewById(R.id.spMonedaActual);
        spMonedaCambio = (Spinner) findViewById(R.id.spMonedaCambio);
        etValorCambiar = (EditText) findViewById(R.id.etValorCambiar);
        tvResultado = (TextView) findViewById(R.id.tvResultado);

        String monedaActual = spMonedaActual.getSelectedItem().toString();
        String monedaCambio = spMonedaCambio.getSelectedItem().toString();
        double valorCambio = Double.parseDouble(etValorCambiar.getText().toString());

        double resultado = procesarConversion(monedaActual, monedaCambio, valorCambio);

        if (resultado > 0) {
            tvResultado.setText( String.format("%5.2f %s =\n%5.2f %s",valorCambio,monedaActual,resultado,monedaCambio));
            etValorCambiar.setText("");

            SharedPreferences preferencias = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferencias.edit();
            editor.putString("monedaActual", monedaActual);
            editor.putString("monedaCambio", monedaCambio);
            editor.commit();
        } else {
            tvResultado.setText(String.format("Usted recibirá: "));
            Toast.makeText(this, "Las opciones elegidas no tienen  un factor de conversión", Toast.LENGTH_SHORT).show();
        }
    }

    private double procesarConversion(String monedaActual,String monedaCambio,double valorCambio) {
        double resultadoConversion = 0;
        switch (monedaActual) {
            case "Dolares":
                if (monedaCambio.equals("Euros")) {
                    resultadoConversion = valorCambio * factorDolarEuro;
                }
                if (monedaCambio.equals("Pesos")) {
                    resultadoConversion = valorCambio / factorPesoDolar;
                }
                break;

            case "Euros":
                if (monedaCambio.equals("Dolares")) {
                    resultadoConversion = valorCambio / factorDolarEuro;
                }
                if (monedaCambio.equals("Pesos")) {
                    resultadoConversion = valorCambio / factorPesoEuro;
                }
                break;
            case "Pesos":
                if (monedaCambio.equals("Dolares")) {
                    resultadoConversion = valorCambio * factorPesoDolar;
                }
                if (monedaCambio.equals("Euros")) {
                    resultadoConversion = valorCambio * factorPesoEuro;
                }
                break;
            default:
                break;
        }
        return resultadoConversion;
    }
}
