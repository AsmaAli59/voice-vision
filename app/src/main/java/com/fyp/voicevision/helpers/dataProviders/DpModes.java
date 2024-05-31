package com.fyp.voicevision.helpers.dataProviders;

import com.fyp.voicevision.R;
import com.fyp.voicevision.helpers.models.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class DpModes {

    public List<HomeItem> getModeList() {
        List<HomeItem> arrayList = new ArrayList<>();
        arrayList.add(new HomeItem(0, "Touch Mode", null, R.color.card_color_1, R.drawable.ic_touch));
        arrayList.add(new HomeItem(1, "Voice Mode", null, R.color.card_color_2, R.drawable.ic_voice));
        return arrayList;
    }

}