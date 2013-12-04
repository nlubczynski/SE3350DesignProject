package com.designproject.controllers;

import java.util.ArrayList;
import java.util.List;

import com.designproject.models.Equipment;
import com.designproject.models.HelperMethods;
import com.designproject.models.InspectionElement;
import com.designproject.models.Equipment.node;
import com.designproject.FireAlertApplication;
import com.designproject.R;

import android.os.Bundle;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

public class EquipmentController extends NavigationDrawerActivity {
	private int pageNum, numPages;
	private InspectionElement[] elements;
	private LinearLayout content;
	private Equipment equipment;

	public void onCreate(Bundle savedInstanceState) {
		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);

		setContentView(R.layout.activity_inspection_form);
		super.onCreate(savedInstanceState);

		// Get the page number
		Intent mIntent = getIntent();
		pageNum = mIntent.getIntExtra("Page Number", 0);

		// Get the application context
		FireAlertApplication app = (FireAlertApplication) getApplication();
		equipment = (Equipment) app.getLocation();

		// Access the various layouts
		content = (LinearLayout) findViewById(R.id.inspect_form_content);
		final LinearLayout header = (LinearLayout) findViewById(R.id.inspect_form_header);
		final LinearLayout endOfContent = (LinearLayout) findViewById(R.id.inspect_form_end_of_content);
		final LinearLayout footer = (LinearLayout) findViewById(R.id.inspect_form_footer);
		final LinearLayout info = (LinearLayout) findViewById(R.id.inspect_form_info);
		final LinearLayout header2 = (LinearLayout) findViewById(R.id.inspect_form_header2);

		// Access the inspection elements and populate the form
		elements = equipment.getInspectionElements();
		populateContent(equipment);

		// Set header containing all information about the equipment and page
		// number
		setTitle(equipment.getName());
		final TextView typeView = new TextView(EquipmentController.this);
		typeView.setText(equipment.getName());
		header.addView(typeView);

		final TextView pageView = new TextView(EquipmentController.this);
		if (numPages > 1) {
			pageView.setText("Page " + pageNum + " of " + numPages);
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, 1);
			pageView.setGravity(Gravity.RIGHT);
			header.addView(pageView, lparams);
		}

		TextView locationView = new TextView(EquipmentController.this);
		String location = equipment.getLocation();
		if (location != null) {
			locationView.setText(location);
			header2.addView(locationView);
		}

		TextView idView = new TextView(EquipmentController.this);
		String id = equipment.getID();
		if (id != null) {
			idView.setText("ID: " + id);
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
					0, LayoutParams.MATCH_PARENT, 1);
			idView.setGravity(Gravity.RIGHT);
			header2.addView(idView, lparams);
		}

		// Create a More Info button and add it to the header
		final node[] attributes = equipment.getAttributes();
		if (attributes.length > 2) {
			final Button moreInfo = new Button(EquipmentController.this);
			moreInfo.setText("More Info");
			LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, 1);
			moreInfo.setTextSize(10);
			moreInfo.setBackgroundColor(getResources().getColor(
					R.color.light_grey));
			lparams.height = 50;
			lparams.gravity = Gravity.RIGHT;
			info.addView(moreInfo, lparams);

			// On click, toggle more/less info
			moreInfo.setOnClickListener(new View.OnClickListener() {
				public void onClick(View view) {
					if (moreInfo.getText().equals("More Info")) {
						for (int i = 0; i < attributes.length; i++) {
							TextView attributeView = new TextView(
									EquipmentController.this);
							attributeView.setText(attributes[i].getName()
									+ ": " + attributes[i].getValue());
							info.addView(attributeView, i + 2);
						}
						moreInfo.setText("Less Info");
						content.setBackgroundColor(getResources().getColor(
								R.color.white));
					}

					else {
						for (int i = attributes.length - 1; i >= 0; i--) {
							if (attributes[i].getName() != "id"
									&& attributes[i].getName() != "location") {
								View attributeView = info.getChildAt(i + 2);
								info.removeView(attributeView);
							}
						}
						moreInfo.setText("More Info");
					}
				}
			});
		}

		// Add a submit button if it is the final page
		final Button submit = new Button(EquipmentController.this);
		if (pageNum == numPages) {
			submit.setText("Submit");
			endOfContent.addView(submit);
		}

		// On click, set all the elements to complete, and request comments on
		// the items that failed
		submit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				List<InspectionElement> failed = new ArrayList<InspectionElement>();

				// Save the data
				updateData();

				// Set everything to complete
				for (InspectionElement e : elements) {
					e.setHasBeenTested();
					if (e.getTestResult() == false) {
						failed.add(e);
					}
				}

				// Clear the screen and present a form requesting comments for
				// all the items that failed
				if (failed.size() > 0) {
					content.removeAllViews();
					endOfContent.removeAllViews();
					footer.removeAllViews();
					header.removeView(pageView);
					ScrollView scroll = new ScrollView(EquipmentController.this);
					content.addView(scroll);
					LinearLayout ll = new LinearLayout(EquipmentController.this);
					ll.setOrientation(LinearLayout.VERTICAL);
					scroll.addView(ll);

					for (int i = 0; i < failed.size(); i++) {
						TextView question = new TextView(
								EquipmentController.this);
						question.setText("Why did " + failed.get(i).getName()
								+ " fail?");
						ll.addView(question);

						EditText answer = new EditText(EquipmentController.this);
						answer.setText(failed.get(i).getTestNotes());
						ll.addView(answer);
					}
					final Button submit = new Button(EquipmentController.this);
					submit.setText("Submit");
					ll.addView(submit);

					// On submit, finish the activity
					submit.setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							finish();
						}
					});
				}
				// If no items failed, finish the activity
				else {
					finish();
				}
			}
		});

		// Add a button to go back a page on all pages except the first
		final ImageButton previous = new ImageButton(EquipmentController.this);
		if (pageNum > 1) {
			previous.setImageResource(R.drawable.previous_page);
			previous.setBackgroundColor(getResources().getColor(
					R.color.lighter_light_grey));
			footer.addView(previous);
		}
		// restart activity for the previous page
		previous.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
				Intent previousPage = new Intent(EquipmentController.this,
						EquipmentController.class);
				previousPage.putExtra("Page Number", --pageNum);
				startActivity(previousPage);
			}
		});

		// Filler
		TextView blank = new TextView(EquipmentController.this);
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
		params.weight = 1.0f;
		footer.addView(blank, params);

		// Add a button to go forward a page on all pages except the last
		final ImageButton next = new ImageButton(EquipmentController.this);
		if (pageNum < numPages) {
			next.setImageResource(R.drawable.next_page);
			next.setBackgroundColor(getResources().getColor(
					R.color.lighter_light_grey));
			footer.addView(next);
		}
		// restart activity for the next page
		next.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				finish();
				Intent nextPage = new Intent(EquipmentController.this,
						EquipmentController.class);
				nextPage.putExtra("Page Number", ++pageNum);
				startActivity(nextPage);
			}
		});
	}

	// Populates the form depending on equipment type
	public void populateContent(Equipment equipment) {
		// Get the type of the equipment
		String type = equipment.getName();

		// If it's a fire extinguisher, the form is all check boxes
		if (type.equals("Extinguisher")) {
			// Ensure that there are the correct number of inspection elements
			if (equipment.getInspectionElements().length < 9) {
				equipment.clearInspectionElements();
				equipment.addInspectionElement(new InspectionElement(
						"Hydro Test"));
				equipment.addInspectionElement(new InspectionElement(
						"6 Year Insp"));
				equipment.addInspectionElement(new InspectionElement("Weight"));
				equipment
						.addInspectionElement(new InspectionElement("Bracket"));
				equipment.addInspectionElement(new InspectionElement("Gauge"));
				equipment
						.addInspectionElement(new InspectionElement("Pull Pin"));
				equipment
						.addInspectionElement(new InspectionElement("Signage"));
				equipment.addInspectionElement(new InspectionElement("Collar"));
				equipment.addInspectionElement(new InspectionElement("Hose"));
			}

			// Add elements to one of two pages
			elements = equipment.getInspectionElements();
			if (pageNum == 1) {
				for (int i = 0; i < 6; i++) {
					CheckBox cb = new CheckBox(EquipmentController.this);
					cb.setText(elements[i].getName());
					cb.setChecked(elements[i].getTestResult());
					content.addView(cb);
				}
			} else {
				for (int i = 6; i < elements.length; i++) {
					CheckBox cb = new CheckBox(EquipmentController.this);
					cb.setText(elements[i].getName());
					cb.setChecked(elements[i].getTestResult());
					content.addView(cb);
				}
			}
			numPages = 2;
		}
		// If it's a fire hose cabinet, the form is all check boxes
		else if (type.equals("FireHoseCabinet")) {
			// Ensure the correct number of inspection elements are present
			if (equipment.getInspectionElements().length < 4) {
				equipment.clearInspectionElements();
				equipment.addInspectionElement(new InspectionElement(
						"Cabinet Condition"));
				equipment.addInspectionElement(new InspectionElement(
						"Valve Condition"));
				equipment.addInspectionElement(new InspectionElement(
						"Hose Re-Rack"));
				equipment.addInspectionElement(new InspectionElement("HT Due"));
			}

			elements = equipment.getInspectionElements();
			numPages = 1;
			// Populate form
			for (int i = 0; i < 4; i++) {
				CheckBox cb = new CheckBox(EquipmentController.this);
				cb.setText(elements[i].getName());
				cb.setChecked(elements[i].getTestResult());
				content.addView(cb);
			}
		}
		// If it's an emergency light the form consists of a combination of
		// check boxes and text fields
		else if (type.equals("EmergencyLight")) {
			// Ensure the correct number of elements are present
			if (equipment.getInspectionElements().length < 5) {
				equipment.clearInspectionElements();
				equipment.addInspectionElement(new InspectionElement(
						"Requires Service or Repair"));
				equipment.addInspectionElement(new InspectionElement(
						"Operation Confirmed"));
				equipment.addInspectionElement(new InspectionElement(
						"Number of Heads"));
				equipment.addInspectionElement(new InspectionElement(
						"Total Power"));
				equipment
						.addInspectionElement(new InspectionElement("Voltage"));
			}

			elements = equipment.getInspectionElements();
			numPages = 1;

			// Populate form
			for (int i = 0; i < 2; i++) {
				CheckBox cb = new CheckBox(EquipmentController.this);
				cb.setText(elements[i].getName());
				cb.setChecked(elements[i].getTestResult());
				content.addView(cb);
			}
			for (int j = 2; j < elements.length; j++) {
				TextView tv = new TextView(EquipmentController.this);
				tv.setText(elements[j].getName());
				content.addView(tv);
				EditText et = new EditText(EquipmentController.this);
				et.setText(elements[j].getTestNotes());
				et.setInputType(InputType.TYPE_CLASS_NUMBER);
				content.addView(et);
			}
		}
		// If it's a kitchen suppression system, the form consists of a
		// combination of check boxes, text fields, and radio buttons
		// Lots of special case handling
		else if (type.equals("KitchenSuppressionSystem")) {
			// Ensure that the correct number of inspection items are present
			if (equipment.getInspectionElements().length < 41) {
				equipment.clearInspectionElements();
				equipment.addInspectionElement(new InspectionElement(
						"System interlock with building fire alarm"));
				equipment.addInspectionElement(new InspectionElement(
						"System discharged"));
				equipment.addInspectionElement(new InspectionElement(
						"All seals intact. No evidence of tampering"));
				equipment.addInspectionElement(new InspectionElement(
						"All appl properly covered w/ correct nozzles"));
				equipment.addInspectionElement(new InspectionElement(
						"Duct & plenum covered w/ correct nozzles"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked positioning of all nozzles"));
				equipment.addInspectionElement(new InspectionElement(
						"Hood/Duct penetrations sealed"));
				equipment.addInspectionElement(new InspectionElement(
						"Grease Accumulation"));
				equipment.addInspectionElement(new InspectionElement(
						"Pressure gauge in proper range"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked cartridge weight"));
				equipment.addInspectionElement(new InspectionElement(
						"Cylinder hydrostatic test date"));
				equipment.addInspectionElement(new InspectionElement(
						"Inspect cylinder and mount"));
				equipment.addInspectionElement(new InspectionElement(
						"Operated system from terminal link"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked travel of cable and link position"));
				equipment.addInspectionElement(new InspectionElement(
						"Fusible links"));
				equipment.addInspectionElement(new InspectionElement(
						"Replaced fusible links"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked and cleaned fusible links"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked operation of manual release"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked operation of micro-switch"));
				equipment.addInspectionElement(new InspectionElement(
						"Checked operation of gas valve"));
				equipment.addInspectionElement(new InspectionElement(
						"Piping/conduit securely bracketed"));
				equipment.addInspectionElement(new InspectionElement(
						"Nozzle cleaned"));
				equipment.addInspectionElement(new InspectionElement(
						"Proper nozzle caps/covers in place"));
				equipment.addInspectionElement(new InspectionElement(
						"Proper clearance flame to filters"));
				equipment.addInspectionElement(new InspectionElement(
						"Proper seperation between fryers and flame"));
				equipment.addInspectionElement(new InspectionElement(
						"Exhaust fan in operating order"));
				equipment.addInspectionElement(new InspectionElement(
						"Manual and remote set seals in place"));
				equipment.addInspectionElement(new InspectionElement(
						"System cart. replaced/safety pins removed"));
				equipment.addInspectionElement(new InspectionElement(
						"System operational and armed"));
				equipment.addInspectionElement(new InspectionElement(
						"Slave system operational and armed"));
				equipment.addInspectionElement(new InspectionElement(
						"Fan warning sign on hood"));
				equipment.addInspectionElement(new InspectionElement(
						"K class fire extinguisher in cooking area"));
				equipment.addInspectionElement(new InspectionElement(
						"2A water type/wet chem type for solid fuel"));
				equipment.addInspectionElement(new InspectionElement(
						"Water hose in area of solid fuel appliance"));
				equipment.addInspectionElement(new InspectionElement(
						"Proper ABC fire extinguisher for other areas"));
				equipment.addInspectionElement(new InspectionElement(
						"Fire extinguisher properly serviced"));
				equipment.addInspectionElement(new InspectionElement(
						"Personnel instructed on manual operation sys."));
				equipment.addInspectionElement(new InspectionElement(
						"Were system monthly insp. performed"));
				equipment.addInspectionElement(new InspectionElement(
						"Personnel instructed on use of fire ext."));
				equipment.addInspectionElement(new InspectionElement(
						"Service and certification tag on system"));
				equipment.addInspectionElement(new InspectionElement(
						"System installed per U.I. 300 standard"));
			}

			elements = equipment.getInspectionElements();
			numPages = 8;

			// Populate the appropriate page of the form
			switch (pageNum) {
			case 1:
				for (int j = 0; j < 6; j++) {
					CheckBox cb = new CheckBox(EquipmentController.this);
					cb.setText(elements[j].getName());
					cb.setChecked(elements[j].getTestResult());
					content.addView(cb);
				}
				break;
			case 2:
				for (int j = 6; j < 10; j++) {
					if (j == 7) {
						TextView grease = new TextView(EquipmentController.this);
						grease.setText(elements[j].getName());
						content.addView(grease);
						final RadioButton[] rb = new RadioButton[3];
						RadioGroup rg = new RadioGroup(this);
						rg.setOrientation(RadioGroup.HORIZONTAL);
						for (int i = 0; i < 3; i++) {
							rb[i] = new RadioButton(this);
							rg.addView(rb[i]);
						}
						rb[0].setText("Normal");
						rb[1].setText("Heavy");
						rb[2].setText("Excess");
						if (elements[j].getTestNotes().equals("Normal"))
							rb[0].setChecked(true);
						else if (elements[j].getTestNotes().equals("Heavy"))
							rb[1].setChecked(true);
						else if (elements[j].getTestNotes().equals("Excess"))
							rb[2].setChecked(true);
						content.addView(rg);
					} else if (j == 9) {
						CheckBox cb = new CheckBox(EquipmentController.this);
						cb.setText(elements[j].getName());
						cb.setChecked(elements[j].getTestResult());
						content.addView(cb);

						TextView lastTest = new TextView(
								EquipmentController.this);
						lastTest.setText("Last Tested");
						content.addView(lastTest);

						EditText ans = new EditText(EquipmentController.this);
						String note = elements[j].getTestNotes();

						CheckBox cb1 = new CheckBox(EquipmentController.this);
						cb1.setText("Replace?");

						if (note.equals("")) {
							ans.setHint("dd/mm/yyyy");
						} else {
							String[] notes = note.split("-");
							if (notes.length == 2) {
								if (notes[0].equals(" "))
									ans.setHint("dd/mm/yyyy");
								else
									ans.setText(notes[0]);
								if (notes[1].equals("Replace"))
									cb1.setChecked(true);
							}
						}
						content.addView(ans);
						content.addView(cb1);
					} else {
						CheckBox cb = new CheckBox(EquipmentController.this);
						cb.setText(elements[j].getName());
						cb.setChecked(elements[j].getTestResult());
						content.addView(cb);
					}
				}
				break;
			case 3:
				for (int j = 10; j < 15; j++) {
					if (j == 10) {
						TextView cylinder = new TextView(
								EquipmentController.this);
						cylinder.setText(elements[j].getName());
						content.addView(cylinder);

						EditText ans = new EditText(EquipmentController.this);
						if (elements[j].getTestNotes().equals(""))
							ans.setHint("dd/mm/yyyy");
						else
							ans.setText(elements[j].getTestNotes());
						content.addView(ans);
					} else if (j == 14) {
						TextView fusible = new TextView(
								EquipmentController.this);
						fusible.setText(elements[j].getName());
						content.addView(fusible);

						final RadioButton[] rb = new RadioButton[3];
						RadioGroup rg = new RadioGroup(this);
						rg.setOrientation(RadioGroup.HORIZONTAL);
						for (int i = 0; i < 3; i++) {
							rb[i] = new RadioButton(this);
							rg.addView(rb[i]);
						}
						rb[0].setText("450");
						rb[1].setText("500");
						rb[2].setText("Other");
						if (elements[j].getTestNotes().equals("450"))
							rb[0].setChecked(true);
						else if (elements[j].getTestNotes().equals("500"))
							rb[1].setChecked(true);
						else if (elements[j].getTestNotes().equals("other"))
							rb[2].setChecked(true);
						content.addView(rg);
					} else {
						CheckBox cb = new CheckBox(EquipmentController.this);
						cb.setText(elements[j].getName());
						cb.setChecked(elements[j].getTestResult());
						content.addView(cb);
					}
				}
				break;
			case 4:
				for (int j = 15; j < 19; j++) {
					if (j == 15) {
						CheckBox cb = new CheckBox(EquipmentController.this);
						cb.setText(elements[j].getName());
						cb.setChecked(elements[j].getTestResult());
						content.addView(cb);

						TextView mfgDate = new TextView(
								EquipmentController.this);
						mfgDate.setText("Mfg. Date");
						content.addView(mfgDate);

						EditText ans = new EditText(EquipmentController.this);
						if (elements[j].getTestNotes().equals(""))
							ans.setHint("dd/mm/yyyy");
						else
							ans.setText(elements[j].getTestNotes());
						content.addView(ans);
					} else {
						CheckBox cb = new CheckBox(EquipmentController.this);
						cb.setText(elements[j].getName());
						cb.setChecked(elements[j].getTestResult());
						content.addView(cb);
					}
				}
				break;
			case 5:
				for (int j = 19; j < 24; j++) {
					CheckBox cb = new CheckBox(EquipmentController.this);
					cb.setText(elements[j].getName());
					cb.setChecked(elements[j].getTestResult());
					content.addView(cb);
					if (j == 19) {
						final RadioButton[] rb = new RadioButton[2];
						RadioGroup rg = new RadioGroup(this);
						rg.setOrientation(RadioGroup.HORIZONTAL);
						for (int i = 0; i < 2; i++) {
							rb[i] = new RadioButton(this);
							rg.addView(rb[i]);
						}
						rb[0].setText("Electrical");
						rb[1].setText("Mechanical");
						if (elements[j].getTestNotes().equals("Electrical"))
							rb[0].setChecked(true);
						else if (elements[j].getTestNotes()
								.equals("Mechanical"))
							rb[1].setChecked(true);
						content.addView(rg);
					}
				}
				break;
			case 6:
			case 7:
			case 8:
				for (int j = ((4 * 6) + ((pageNum - 6) * 6)); j < Math.min(
						((4 * 6) + ((pageNum - 6) * 6) + 6), elements.length); j++) {
					CheckBox cb = new CheckBox(EquipmentController.this);
					cb.setText(elements[j].getName());
					cb.setChecked(elements[j].getTestResult());
					content.addView(cb);
				}
				break;
			}
		}
	}

	@Override
	public void onResume() {
		// Check if Logged in
		HelperMethods.logOutHandler(HelperMethods.CHECK_IF_LOGGED_IN, this);

		super.onResume();
	}

	@Override
	protected void onDestroy() {
		updateData();
		super.onDestroy();
	}

	// To complete the activity, update the data then call super.finish()
	public void finish() {
		updateData();
		super.finish();
	}

	// Accesses the information from the form and updates the inspection
	// elements
	private void updateData() {
		for (int i = content.getChildCount() - 1; i >= 0; i--) {
			// If the element is a check box, set test result to the value of
			// the check box
			if (content.getChildAt(i) instanceof CheckBox) {
				CheckBox cbView = (CheckBox) content.getChildAt(i);
				String text = (String) cbView.getText();
				boolean value = cbView.isChecked();
				for (InspectionElement element : elements) {
					if (element.getName().equals(text)) {
						element.setTestResult(value);
					}
				}
			}
			// If the element is a radio group, set the test note of the
			// inspection element to the value of the selected radio button
			// and set the test result to pass/fail based on the value of the
			// selected radio button
			else if (content.getChildAt(i) instanceof RadioGroup) {
				String item = ((TextView) content.getChildAt(i - 1)).getText()
						.toString();
				RadioGroup rgView = (RadioGroup) content.getChildAt(i);
				for (int j = 0; j < rgView.getChildCount(); j++) {
					RadioButton rb = (RadioButton) rgView.getChildAt(j);
					if (rb.isChecked()) {
						for (InspectionElement element : elements) {
							if (element.getName().equals(item)) {
								String result = rb.getText().toString();
								if (element.getName().equals(
										"Grease Accumulation")) {
									if (result.equals("Normal"))
										element.setTestResult(true);
									else
										element.setTestResult(false);
								} else if (element.getName().equals(
										"Fusible links 360")) {
									if (result.equals("Other"))
										element.setTestResult(false);
									else
										element.setTestResult(true);
								}
								element.setTestNotes(result);
							}
						}
					}
				}
			}
			// Special handling for each instance of EditText
			else if (content.getChildAt(i) instanceof EditText) {
				String answer = " ";
				if (((EditText) content.getChildAt(i)).getText() != null) {
					answer = ((EditText) content.getChildAt(i)).getText()
							.toString();
				}
				String item = "";
				if (((TextView) content.getChildAt(i - 1)).getText() != null) {
					item = ((TextView) content.getChildAt(i - 1)).getText()
							.toString();
				}
				if (item.equals("Cylinder hydrostatic test date")) {
					elements[10].setHasBeenTested();
					elements[10].setTestResult(true);
					elements[10].setTestNotes(answer);
				} else if (item.equals("Mfg. Date")) {
					elements[15].setTestNotes(answer);
				} else if (item.equals("Last Tested")) {
					CheckBox cbView = (CheckBox) content.getChildAt(i + 1);
					boolean value = cbView.isChecked();
					String note = answer + "-" + (value ? "Replace" : " ");
					elements[9].setTestNotes(note);
				}
				for (InspectionElement element : elements) {
					if (element.getName().equals(item)) {
						element.setTestResult(true);
						element.setTestNotes(answer);
					}
				}
			}
			// If the element is a scroll view, it is the final page asking why
			// inspection elements were failed
			// The entered text is recorded as test notes
			else if (content.getChildAt(i) instanceof ScrollView) {
				ScrollView sv = (ScrollView) content.getChildAt(i);
				LinearLayout ll = (LinearLayout) sv.getChildAt(0);
				for (int j = 0; j < ll.getChildCount(); j++) {
					if (ll.getChildAt(j) instanceof TextView
							&& !(ll.getChildAt(j) instanceof EditText)
							&& !(ll.getChildAt(j) instanceof Button)) {
						TextView tvView = (TextView) ll.getChildAt(j);
						EditText etView = (EditText) ll.getChildAt(j + 1);

						String value = "", text = "";
						String[] values = {};
						if (tvView.getText() != null) {
							values = tvView.getText().toString().split(" ");
							for (int k = 2; k < values.length - 1; k++) {
								if (k != 2)
									text += " ";
								text += values[k];
							}
						}
						if (etView.getText() != null) {
							value = etView.getText().toString();
						}

						for (InspectionElement element : elements) {
							if (element.getName().equals(text)) {
								element.setTestNotes(value);
							}
						}
					}
				}
			}
		}
	}
}
