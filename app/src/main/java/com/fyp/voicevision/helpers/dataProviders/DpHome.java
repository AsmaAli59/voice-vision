package com.fyp.voicevision.helpers.dataProviders;

import com.fyp.voicevision.R;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class DpHome {

    public List<HomeItem> getHomeFeatureList() {
        List<HomeItem> arrayList = new ArrayList<>();
        arrayList.add(new HomeItem(0, "Class room", "Immerse yourself in video lectures.", R.color.card_color_1, R.drawable.img_home_classroom));
        arrayList.add(new HomeItem(1, "Phrases", "Expand your vocabulary with useful phrases.", R.color.card_color_2, R.drawable.img_home_vocabulary));
        arrayList.add(new HomeItem(2, "Quiz", "Test your knowledge with interactive quizzes.", R.color.card_color_3, R.drawable.img_home_quiz));
        return arrayList;
    }

}