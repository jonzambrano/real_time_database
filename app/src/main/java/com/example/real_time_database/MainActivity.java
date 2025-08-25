package com.example.real_time_database;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    DatabaseReference HumedadRef, PresionRef, VelocidadRef, TemperaturaRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Opción 1: Usar el ScrollView como contenedor principal
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Conexion a la base de datos
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        HumedadRef = database.getReference("SensoresIoT/humedad");
        PresionRef = database.getReference("SensoresIoT/presion");
        VelocidadRef = database.getReference("SensoresIoT/velocidad");
        TemperaturaRef = database.getReference("SensoresIoT/temperatura");

        // Read database
        TextView txt_humedad = findViewById(R.id.valor_Humedad);
        HumedadRef.addValueEventListener(setListener(txt_humedad, "%"));

        TextView txt_presion = findViewById(R.id.valor_Presion);
        PresionRef.addValueEventListener(setListener(txt_presion, " hPa"));

        TextView txt_temperatura = findViewById(R.id.valor_Temperatura);
        TemperaturaRef.addValueEventListener(setListener(txt_temperatura, "°C"));

        TextView txt_velocidad = findViewById(R.id.valor_Velocidad);
        VelocidadRef.addValueEventListener(setListener(txt_velocidad, " km/h"));
    }

    public ValueEventListener setListener(TextView txt, String UnidadMedida) {
        return (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getValue() != null) {
                    txt.setText(snapshot.getValue().toString() + " " + UnidadMedida);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                txt.setText("Error: " + error.getMessage());
            }
        });
    }

    // Método corregido para el botón de Temperatura
    public void clickBotonTemp(View view) {
        try {
            EditText txt_temp_edit = findViewById(R.id.setvalor_Temperatura);
            String valor = txt_temp_edit.getText().toString().trim();

            if (!valor.isEmpty()) {
                float temperatura = Float.parseFloat(valor);
                TemperaturaRef.setValue(temperatura);
                Toast.makeText(this, "Temperatura actualizada: " + temperatura + "°C", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Por favor ingrese un valor", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    // Método agregado para el botón de Humedad
    public void clickBotonHumedad(View view) {
        try {
            EditText txt_humedad_edit = findViewById(R.id.setvalor_Humedad);
            String valor = txt_humedad_edit.getText().toString().trim();

            if (!valor.isEmpty()) {
                float humedad = Float.parseFloat(valor);
                HumedadRef.setValue(humedad);
                Toast.makeText(this, "Humedad actualizada: " + humedad + "%", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Por favor ingrese un valor", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Por favor ingrese un número válido", Toast.LENGTH_SHORT).show();
        }
    }

    // NOTA: Para agregar funcionalidad de Presión y Velocidad necesitarías:
    // 1. Agregar EditText con id="setvalor_Presion" y id="setvalor_Velocidad" en el XML
    // 2. Agregar botones con onClick="clickBotonPresion" y onClick="clickBotonVelocidad"
    // 3. Descomentar los métodos siguientes:

    /*
    public void clickBotonPresion(View view) {
        // Implementar cuando se agregue el EditText correspondiente en el XML
    }

    public void clickBotonVelocidad(View view) {
        // Implementar cuando se agregue el EditText correspondiente en el XML
    }
    */
}