package illeagle99.ordercalculator;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.PrintStream;
import java.util.Scanner;

import illeagle99.ordercalculator.backend.Item;
import illeagle99.ordercalculator.backend.Receipt;

public class MainActivity extends AppCompatActivity {
    private Receipt receipt = new Receipt();
    private File taxFile;
    private EditText salesTaxInput;
    private Dialog salesTaxDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        taxFile = new File(getFilesDir() + File.separator + "salestax");
        salesTaxInput = new EditText(this);
        setupDialog();
        getTaxRate();
        setupText();
        setupListView();
    }

    private void setupListView() {
        ListView view = (ListView) findViewById(R.id.listview);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        for (int x = 0; x < receipt.size(); x++) adapter.add(receipt.get(x).getName());

        view.setAdapter(adapter);
        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                receipt.select(receipt.get(i));
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBinder("receipt", receipt);
                intent.putExtra("receipt", bundle);
                startActivity(intent);
            }
        });

        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(MainActivity.this, receipt.get(i).getName() + " was removed!", Toast.LENGTH_SHORT).show();
                receipt.remove(i);
                setupListView();
                return false;
            }
        });
    }

    private void setupText() {
        String stax = String.format("Sales Tax: %.2f", receipt.getTaxRate());
        ((Button) findViewById(R.id.button3)).setText(stax);
    }

    /* new item */
    public void onClick(View v) {
        Intent intent = new Intent(this, ItemActivity.class);
        Bundle bundle = new Bundle();
        receipt.select(null);
        bundle.putBinder("receipt", receipt);
        intent.putExtra("receipt", bundle);
        startActivity(intent);
    }

    /* calculate & receipt */
    public void createReceipt(View v) {
        applyShippingFee();
        Intent intent = new Intent(this, ReceiptActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBinder("receipt", receipt);
        intent.putExtra("receipt", bundle);
        startActivity(intent);
    }

    /* set sales tax */
    public void setSalesTax(View v) {
        salesTaxDialog.show();
    }

    private void setupDialog() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Sales Tax");
        dialog.setMessage("set sales tax (eg. 0.07): ");
        dialog.setView(salesTaxInput);
        dialog.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dInt, int id) {
                String text = salesTaxInput.getText().toString();
                double taxRate = handleUserStupidity(text);
                if (taxRate == -1) {
                    Toast.makeText(MainActivity.this, "input not understood", Toast.LENGTH_SHORT).show();
                    return;
                }
                receipt.setTaxRate(taxRate);
                setupText();
                saveTaxRate();
            }
        });
        salesTaxDialog = dialog.create();
    }

    private double handleUserStupidity(String input) {
        try {
            Double dInput = Double.parseDouble(input);
            if (dInput < 0 || dInput >= 1) return -1;
            return dInput;
        } catch (Exception e) {
            return -1;
        }
    }

    public void applyShippingFee() {
        CheckBox shippingFee = (CheckBox) findViewById(R.id.shippingfee);
        if (shippingFee.isChecked()) {
            if (!receipt.contains("shipping fee")) {
                receipt.add(new Item("shipping fee", 0.75, true));
            }
        } else {
            if (receipt.contains("shipping fee")) {
                receipt.remove("shipping fee");
            }
        }
    }

    private void saveTaxRate() {
        try {
            System.setOut(new PrintStream(taxFile));
            System.out.print(receipt.getTaxRate());
            System.out.close();
        } catch (Exception e) {
            System.out.println("SAVE TAX RATE : " + e);
        }
    }

    private void getTaxRate() {
        try {
            if (!taxFile.exists()) {
                taxFile.createNewFile();
                receipt.setTaxRate(0.07); /* new jersey */
                return;
            } else {
                Scanner reader = new Scanner(taxFile);
                receipt.setTaxRate(Double.parseDouble(reader.next()));
                reader.close();
            }
        } catch (Exception e) {
            System.out.println("SET_SALES_TAX, EXCEPTION : " + e);
        }
    }

    public void onResume() {
        setupListView();
        super.onResume();
    }
}
