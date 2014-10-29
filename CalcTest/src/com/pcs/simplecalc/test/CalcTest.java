package com.pcs.simplecalc.test;

import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pcs.simplecalc.Calculate;

public class CalcTest extends ActivityInstrumentationTestCase2<Calculate>{

	//Declaring the Views
	public EditText editValue1, editValue2;
	public TextView res;
	public Button btnAdd;
	private Button btnSub;
	private Button btnMul;
	private Button btnDiv;
	private Button btnClr;
	private Calculate activity;
	public   String VALUE1 = "6";
	public   String VALUE2 ="6";
	public int result;

	public CalcTest() {
		super(Calculate.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		activity = getActivity();
		assertNotNull(activity);//Check if Activity exists
		//Getting the Views
		editValue1 = (EditText) activity
				.findViewById(com.pcs.simplecalc.R.id.value1);
		editValue2 = (EditText) activity
				.findViewById(com.pcs.simplecalc.R.id.value2);
		btnAdd = (Button) activity
				.findViewById(com.pcs.simplecalc.R.id.add);
		btnSub = (Button) activity
				.findViewById(com.pcs.simplecalc.R.id.sub);

		btnMul = (Button) activity
				.findViewById(com.pcs.simplecalc.R.id.multiply);

		btnDiv = (Button) activity
				.findViewById(com.pcs.simplecalc.R.id.divide);
		btnClr = (Button) activity
				.findViewById(com.pcs.simplecalc.R.id.clr);

	}
	@Override
	protected void tearDown() throws Exception {

		super.tearDown();

	}


	public void testPreconditions() {

		assertNotNull("Failed getting activity context", getActivity());
		assertNotNull("Can't find a view... Has layout changed?", editValue1);
		assertNotNull("Can't find a view... Has layout changed?", editValue2);
		assertNotNull("Can't find a view... Has layout changed?", btnClr);
		assertNotNull("Can't find a view... Has layout changed?", btnAdd);
		assertNotNull("Can't find a view... Has layout changed?", btnSub);
		assertNotNull("Can't find a view... Has layout changed?", btnMul);
		assertNotNull("Can't find a view... Has layout changed?", btnDiv);


	}

	public void testAdd() {
		result=10;
		//Editing the Values on which operations to be performed.
		TouchUtils.tapView(CalcTest.this, editValue1);
		getInstrumentation().sendStringSync(VALUE1);

		TouchUtils.tapView(CalcTest.this, editValue2);
		getInstrumentation().sendStringSync(VALUE2);
		//Clicks Add Button,Call for add() metrhod
		TouchUtils.clickView(CalcTest.this, btnAdd);
		res = (TextView) activity
				.findViewById(com.pcs.simplecalc.R.id.result);
		int actual = Integer.parseInt(res.getText().toString());
		assertEquals(result, actual);


	}
	public void testSub() {
		//Giving the values
		TouchUtils.tapView(CalcTest.this, editValue1);
		getInstrumentation().sendStringSync(VALUE1);

		TouchUtils.tapView(CalcTest.this, editValue2);
		getInstrumentation().sendStringSync(VALUE2);

		//Clicks Subtraction method,Calls subtract()
		TouchUtils.clickView(CalcTest.this, btnSub);


	}
	public void testMul() {
		TouchUtils.tapView(CalcTest.this, editValue1);
		getInstrumentation().sendStringSync(VALUE1);

		TouchUtils.tapView(CalcTest.this, editValue2);
		getInstrumentation().sendStringSync(VALUE2);

		TouchUtils.clickView(CalcTest.this, btnMul);


	}
	public void testDiv() {
		TouchUtils.tapView(CalcTest.this, editValue1);
		getInstrumentation().sendStringSync(VALUE1);

		TouchUtils.tapView(CalcTest.this, editValue2);
		getInstrumentation().sendStringSync(VALUE2);

		TouchUtils.clickView(CalcTest.this, btnDiv);


	}

}
