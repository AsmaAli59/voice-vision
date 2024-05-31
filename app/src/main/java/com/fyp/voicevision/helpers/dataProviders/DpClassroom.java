package com.fyp.voicevision.helpers.dataProviders;

import com.fyp.voicevision.helpers.models.ClassroomItem;
import com.fyp.voicevision.helpers.models.VocabularyItem;

import java.util.ArrayList;
import java.util.List;

public class DpClassroom {

    public List<ClassroomItem> getClassroomList() {
        List<ClassroomItem> arrayList = new ArrayList<>();
        arrayList.add(new ClassroomItem("0", "Course Lecture 1", false));
        arrayList.add(new ClassroomItem("1", "Course Lecture 2", false));
        arrayList.add(new ClassroomItem("2", "Course Lecture 3", false));
        arrayList.add(new ClassroomItem("3", "Course Lecture 4", true));
        arrayList.add(new ClassroomItem("4", "Course Lecture 5", true));
        arrayList.add(new ClassroomItem("5", "Course Lecture 6", true));
        arrayList.add(new ClassroomItem("6", "Course Lecture 7", true));
        arrayList.add(new ClassroomItem("7", "Course Lecture 8", true));
        return arrayList;
    }

}
