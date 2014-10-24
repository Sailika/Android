package com.pcs.simplecalc;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Calculate extends Activity implements OnClickListener {

	public TextView result;
	public EditText value1;
	public EditText value2;
	public int op1;
	public int op2;
	public int temp;
	public Button clr;
	public Button add;
	public Button sub;
	public Button mul;
	public Button div;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_calculate);

		clr = (Button) findViewById(R.id.clr);
		add = (Button) findViewById(R.id.add);

		sub = (Button) findViewById(R.id.sub);

		mul = (Button) findViewById(R.id.multiply);

		div = (Button) findViewById(R.id.divide);

		clr.setOnClickListener(this);
		add.setOnClickListener(this);
		sub.setOnClickListener(this);
		mul.setOnClickListener(this);
		div.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		value1 = (EditText) findViewById(R.id.value1);
		value2 = (EditText) findViewById(R.id.value2);
		result = (TextView) findViewById(R.id.result);
		switch (v.getId()) {
		case R.id.clr:
			result.setText("");
			value1.setText("");
			value2.setText("");
			Toast.makeText(Calculate.this,getResources().getString(R.string.clrMsg), Toast.LENGTH_SHORT).show();
			break;
		case R.id.add:
			op1 = Integer.parseInt(value1.getText().toString());
			op2 = Integer.parseInt(value2.getText().toString());
			temp = Math.add(op1, op2);
			result.setText(Integer.toString(temp));
			Toast.makeText(Calculate.this,getResources().getString(R.string.addMsg), Toast.LENGTH_SHORT).show();
			break;
		case R.id.sub:
			op1 = Integer.parseInt(value1.getText().toString());
			op2 = Integer.parseInt(value2.getText().toString());
			temp = Math.subtract(op1, op2);
			result.setText(Integer.toString(temp));
			Toast.makeText(Calculate.this,getResources().getString(R.string.subMsg), Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.multiply:
			op1 = Integer.parseInt(value1.getText().toString());
			op2 = Integer.parseInt(value2.getText().toString());
			temp = Math.mul(op1, op2);
			result.setText(Integer.toString(temp));
			Toast.makeText(Calculate.this,getResources().getString(R.string.mulMsg), Toast.LENGTH_SHORT).show();
			break;
			
		case R.id.divide:
			op1 = Integer.parseInt(value1.getText().toString());
			op2 = Integer.parseInt(value2.getText().toString());
			temp = Math.div(op1, op2);
			result.setText(Integer.toString(temp));
			Toast.makeText(Calculate.this,getResources().getString(R.string.divMsg), Toast.LENGTH_LONG).show();
			break;

		default:
			break;
		}
	}

}
