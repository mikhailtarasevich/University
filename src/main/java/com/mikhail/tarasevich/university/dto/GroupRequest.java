package com.mikhail.tarasevich.university.dto;

import com.mikhail.tarasevich.university.entity.EducationForm;
import com.mikhail.tarasevich.university.entity.Faculty;
import com.mikhail.tarasevich.university.entity.Student;

import java.util.Objects;

public class GroupRequest extends DtoWithUniqueName{

    private Faculty faculty;
    private Student headStudent;
    private EducationForm educationForm;

    public GroupRequest() {
    }

    public GroupRequest(int id, String name, Faculty faculty, Student headStudent, EducationForm educationForm) {
        super(id, name);
        this.faculty = faculty;
        this.headStudent = headStudent;
        this.educationForm = educationForm;
    }

    public Faculty getFaculty() {
        return faculty;
    }

    public void setFaculty(Faculty faculty) {
        this.faculty = faculty;
    }

    public Student getHeadStudent() {
        return headStudent;
    }

    public void setHeadStudent(Student headStudent) {
        this.headStudent = headStudent;
    }

    public EducationForm getEducationForm() {
        return educationForm;
    }

    public void setEducationForm(EducationForm educationForm) {
        this.educationForm = educationForm;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GroupRequest)) return false;
        if (!super.equals(o)) return false;
        GroupRequest that = (GroupRequest) o;
        return Objects.equals(getFaculty(), that.getFaculty()) && Objects.equals(getHeadStudent(), that.getHeadStudent()) && Objects.equals(getEducationForm(), that.getEducationForm());
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFaculty(), getHeadStudent(), getEducationForm());
    }

    @Override
    public String toString() {
        return "GroupRequest{" +
                "id=" + id +
                ", name=" + name +
                ", faculty=" + faculty +
                ", headStudent=" + headStudent +
                ", educationForm=" + educationForm +
                '}';
    }

}
