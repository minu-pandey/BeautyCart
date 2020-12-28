package com.juhi.Util;

import com.juhi.R;
import com.juhi.model.PackageItem;

import java.util.ArrayList;
import java.util.List;

public class PackageListProvider {
    private static List<PackageItem> packageItemList=new ArrayList<>();
    private static int[] resourceId =
            {
                    R.drawable.ricawaxing, R.drawable.hair, R.drawable.massage, R.drawable.mehendi, R.drawable.menicure, R.drawable.pedicure, R.drawable.threading
            };
    private static String[] title = {"Body polish & wrap", "Hair styling", "Massage", "Mehendi", "Menicure", "Pedicure", "Threading"};
    private static String[] CollectionName = {"BODY POLISH & WRAP", "HAIR STYLING", "MASSAGE", "MEHENDI", "MENICURE", "PEDICURE", "THREADING"};

    public static List<PackageItem> getPackageItemList() {
        if (packageItemList.isEmpty()) {
            for (int i = 0; i < resourceId.length; i++) {
                packageItemList.add(new PackageItem(resourceId[i], title[i],CollectionName[i]));

            }
        }
        return packageItemList;
    }
}
