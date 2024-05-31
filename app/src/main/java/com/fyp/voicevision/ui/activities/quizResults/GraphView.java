package com.fyp.voicevision.ui.activities.quizResults;

import static com.fyp.voicevision.ui.activities.quizResults.EnlistQuizResults.quizResultList;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.fyp.voicevision.databinding.ActivityGraphViewBinding;
import com.fyp.voicevision.helpers.enums.LevelType;
import com.fyp.voicevision.helpers.models.QuizResult;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

public class GraphView extends AppCompatActivity {

    private ActivityGraphViewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGraphViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setGraph();

        binding.toolbarGraphView.setNavigationOnClickListener(v -> finish());
    }

    private void setGraph() {
        String title = "Quiz Results";
        float basic = 0, intermediate = 0, advance = 0;

        for (QuizResult quizResult : quizResultList) {
            if (quizResult.getLevelType().equals(LevelType.basic.toString())) {
                basic++;
            } else if (quizResult.getLevelType().equals(LevelType.intermediate.toString())) {
                intermediate++;
            } else {
                advance++;
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, basic));
        entries.add(new BarEntry(1, intermediate));
        entries.add(new BarEntry(2, advance));

        BarDataSet dataSet = new BarDataSet(entries, title); // Label for the dataset

        BarData barData = new BarData(dataSet);
        barData.setBarWidth(0.8f); // Set custom bar width if needed

        binding.barChartGraphView.setData(barData);
        binding.barChartGraphView.setFitBars(true);
        binding.barChartGraphView.getDescription().setEnabled(false);
        binding.barChartGraphView.animateY(1000);
        binding.barChartGraphView.invalidate(); // Refresh the chart

        // Customize X-axis labels
        XAxis xAxis = binding.barChartGraphView.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(new String[]{"Basic", "Intermediate", "Advanced"}));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        // Customize Y-axis labels
        YAxis leftAxis = binding.barChartGraphView.getAxisLeft();
        leftAxis.setAxisMinimum(0); // Set the minimum value for the y-axis
        leftAxis.setAxisMaximum(100); // Set the maximum value for the y-axis

        binding.barChartGraphView.getAxisRight().setEnabled(false);
    }
}