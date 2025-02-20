package com.example.gestioncamara;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.Manifest;

public class MainActivity extends AppCompatActivity {

    // Código de solicitud para identificar el permiso de cámara.
    private static final int REQUEST_CAMERA_PERMISSION = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Activa la compatibilidad con diseño EdgeToEdge para ocupar toda la pantalla.
        EdgeToEdge.enable(this);

        // Asocia la vista principal con el archivo XML de diseño.
        setContentView(R.layout.activity_main);

        // Configuración para gestionar los márgenes relacionados con barras del sistema (estado/navegación).
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Busca el botón de la cámara en la vista.
        Button btnCamera = findViewById(R.id.btnCamera);

        // Establece un listener para manejar el clic en el botón.
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Llama al método para gestionar los permisos de la cámara.
                handleCameraPermission();
            }
        });
    }

    // Método que gestiona el flujo del permiso de cámara.
    private void handleCameraPermission() {
        // Comprueba si el permiso de cámara ya ha sido concedido.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {

            // Verifica si es necesario mostrar una explicación al usuario.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                // Si es necesario, muestra un mensaje explicando por qué se necesita el permiso.
                Toast.makeText(this, "Se necesita acceso a la cámara para tomar fotos.", Toast.LENGTH_SHORT).show();
            }

            // Solicita el permiso de cámara al usuario.
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
        } else {
            // Si el permiso ya fue concedido, abre directamente la cámara.
            openCamera();
        }
    }

    // Método que abre la aplicación de la cámara.
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(cameraIntent); // Lanza la intención para tomar una foto.
    }

    // Método que gestiona la respuesta del usuario al diálogo de permisos.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Verifica si la respuesta es para el permiso de cámara.
        if (requestCode == REQUEST_CAMERA_PERMISSION) {
            // Comprueba si el permiso fue concedido.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Si el permiso fue concedido, abre la cámara.
                openCamera();
            } else {
                // Si el permiso fue denegado, muestra un mensaje informativo al usuario.
                Toast.makeText(this, "Permiso de cámara denegado.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
