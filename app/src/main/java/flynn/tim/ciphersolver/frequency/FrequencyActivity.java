/*
 * Copyright (C)  2017-2018 Tran Le Duy
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package flynn.tim.ciphersolver.frequency;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.duy.common.views.ViewUtils;
import com.duy.text.converter.R;
import com.duy.text.converter.activities.base.BaseActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class FrequencyActivity extends BaseActivity implements TextWatcher {
    private BarChart mChart;
    private EditText mInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frequency);
        setupToolbar();
        setTitle(R.string.title_frequency_analysis);

        mChart = findViewById(R.id.chart_view);
        initChart();

        mInput = findViewById(R.id.edit_input);
        mInput.addTextChangedListener(this);
    }

    private void initChart() {
        mChart.setDescription("");

        mChart.getAxisLeft().setDrawLabels(false);
        mChart.getAxisRight().setDrawLabels(false);

        mChart.getXAxis().setTextColor(ViewUtils.getColorFromAttr(this, android.R.attr.textColorPrimary));
        mChart.getXAxis().setLabelsToSkip(0);

        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
    }

    private void drawChart() {


        HashMap<Character, Integer> analyze = FrequencyAnalysis.analyze(mInput.getText().toString());
        ArrayList<String> labels = getLabels(analyze);

        ArrayList<BarEntry> yVals = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : analyze.entrySet()) {
            yVals.add(new BarEntry(entry.getValue(), labels.indexOf(entry.getKey() + "")));
        }

        BarDataSet dataSet = new BarDataSet(yVals, "");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                return String.valueOf((int) value);
            }
        });

        BarData barData = new BarData(labels, dataSet);
        barData.setValueTextColor(ViewUtils.getColorFromAttr(this, android.R.attr.textColorPrimary));

        setData(barData);
    }

    private void setData(BarData barData) {
        mChart.setData(barData);
        mChart.animateY(500);
        //mChart.invalidate();
    }

    private ArrayList<String> getLabels(HashMap<Character, Integer> analyze) {
        ArrayList<String> labels = new ArrayList<>();
        for (Map.Entry<Character, Integer> entry : analyze.entrySet()) {
            labels.add(String.valueOf(entry.getKey()));
        }
        Collections.sort(labels, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        return labels;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        drawChart();
    }
}
