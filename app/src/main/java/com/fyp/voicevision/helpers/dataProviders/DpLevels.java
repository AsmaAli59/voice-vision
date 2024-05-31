package com.fyp.voicevision.helpers.dataProviders;

import com.fyp.voicevision.R;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class DpLevels {

    public List<HomeItem> getLevelList() {
        List<HomeItem> arrayList = new ArrayList<>();
        arrayList.add(new HomeItem(0, "Basic", null, R.color.card_color_1, R.drawable.ic_basic));
        arrayList.add(new HomeItem(1, "Intermediate", null, R.color.card_color_2, R.drawable.ic_intermediate));
        arrayList.add(new HomeItem(2, "Advance", null, R.color.card_color_3, R.drawable.ic_advance));
        return arrayList;
    }

}