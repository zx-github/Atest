package com.csizg.imetest.autotouch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2019/4/4.
 *
 * @description：
 */

public class XYtouch {

    //牛盾
    public static List<float[]> getNiuDunFloatList() {
        List<float[]> list = new ArrayList<>();
        list.add(new float[]{110, 810}); // 横坐标相差140， 纵坐标相差100
        list.add(new float[]{330, 910}); //2
        list.add(new float[]{470, 910}); //3
        list.add(new float[]{190, 1010}); //4
        list.add(new float[]{330, 1010}); //5
        list.add(new float[]{470, 1010}); //6
        list.add(new float[]{190, 1110}); //7
        list.add(new float[]{330, 1110}); //8
        list.add(new float[]{470, 1110}); //9
        return list;
    }
    //搜狗
    public static List<float[]> getSouGouFloatList() {
        List<float[]> list = new ArrayList<>();
        list.add(new float[]{110, 790}); // 横坐标相差140， 纵坐标相差100
        list.add(new float[]{360, 890}); //2
        list.add(new float[]{500, 890}); //3
        list.add(new float[]{220, 990}); //4
        list.add(new float[]{360, 990}); //5
        list.add(new float[]{500, 990}); //6
        list.add(new float[]{220, 1090}); //7
        list.add(new float[]{360, 1090}); //8
        list.add(new float[]{500, 1090}); //9
        return list;
    }
    //百度
    public static List<float[]> getBaiDuFloatList() {
        List<float[]> list = new ArrayList<>();
        list.add(new float[]{110, 800}); // 横坐标相差140， 纵坐标相差100
        list.add(new float[]{330, 900}); //2
        list.add(new float[]{470, 900}); //3
        list.add(new float[]{190, 1000}); //4
        list.add(new float[]{330, 1000}); //5
        list.add(new float[]{470, 1000}); //6
        list.add(new float[]{190, 1100}); //7
        list.add(new float[]{330, 1100}); //8
        list.add(new float[]{470, 1100}); //9
        return list;
    }


}
