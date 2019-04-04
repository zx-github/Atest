package com.csizg.testjni;

/**
 * Created by Leo on 2019/3/14.
 *
 * @description：
 */

public class StuInfo {
    private int stuId;
    private String stuName;
    private int stuAge;
    private String className;

    public StuInfo(int stuId, String stuName, int stuAge, String className) {
        super();
        this.stuId = stuId;
        this.stuName = stuName;
        this.stuAge = stuAge;
        this.className = className;
    }

    public int getStuId() {
        return stuId;
    }

    public void setStuId(int stuId) {
        this.stuId = stuId;
    }

    public String getStuName() {
        return stuName;
    }

    public void setStuName(String stuName) {
        this.stuName = stuName;
    }

    public int getStuAge() {
        return stuAge;
    }

    public void setStuAge(int stuAge) {
        this.stuAge = stuAge;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "StuInfo [stuId=" + stuId + ", stuName=" + stuName + ", stuAge=" + stuAge + ", className=" + className
                + "]";
    }
}
