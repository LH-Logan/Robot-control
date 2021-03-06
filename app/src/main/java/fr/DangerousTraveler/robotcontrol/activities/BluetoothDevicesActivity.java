package fr.DangerousTraveler.robotcontrol.activities;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Set;

import fr.DangerousTraveler.robotcontrol.R;

public class BluetoothDevicesActivity extends AppCompatActivity {

    private BluetoothAdapter bluetoothAdapter = null;
    private ListView pairedDevicesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth_devices);

        pairedDevicesList = findViewById(R.id.bt_paired_devices_list);

        // initialisation du bluetoothAdapter avec le module bluetooth de l'appareil
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        // afficher la liste des appareils appairés
        showPairedDevicesList();
    }

    // méthode permettant d'afficher la liste des appareils déjà appairés
    private void showPairedDevicesList() {

        // obtenir la liste des appareils appairés
        Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();
        ArrayList<String> list = new ArrayList<>();

        // vérifier s'il y a des appareils appairés
        if (pairedDevices.size() >0) {

            // pour chaque appareil apparé, l'ajouter dans la liste avec son nom et son adresse MAC
            for (BluetoothDevice device : pairedDevices) {

                list.add(device.getName() + "\n" + device.getAddress());
            }
        }

        //afficher les appareils appairés dans la recyclerView
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        pairedDevicesList.setAdapter(adapter);

        // gérer le click sur un appareil appairé se situant dans la liste
        pairedDevicesList.setOnItemClickListener(onClickPairedDeviceListener);
    }

    // méthode permettant de gérer le click sur un appareil appairés de la liste pour tenter d'établir une connexion
    private AdapterView.OnItemClickListener onClickPairedDeviceListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView adapterView, View view, int position, long id) {

            /* obtenir l'adresse MAC de l'appareil sélectionné.
             * Cela correspond aux 17 derniers caractères dans la vue */
            String info = ((TextView) view).getText().toString();
            String address = info.substring(info.length() -17);

            // afficher MainActivity et lui envoyer l'adresse MAC de l'appareil auquel se connecter, en indiquant que l'opération a fonctionnée
            Intent intent = new Intent(BluetoothDevicesActivity.this, MainActivity.class);
            intent.putExtra("EXTRA_ADDRESS", address);
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}
