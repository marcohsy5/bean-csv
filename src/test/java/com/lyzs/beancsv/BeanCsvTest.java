package com.lyzs.beancsv;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.lyzs.annotation.CsvColumn;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

public class BeanCsvTest {

    public static class Person {
        @CsvColumn(orderKey = "A")
        private String id;

        @CsvColumn
        private String firstName;

        @CsvColumn
        private String lastName;

        @CsvColumn(format = "yyyyMMdd")
        private Date birthday;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public Date getBirthday() {
            return birthday;
        }

        public void setBirthday(Date birthday) {
            this.birthday = birthday;
        }
    }

    public static class Worker extends Person {
        @CsvColumn
        private int level;
        @CsvColumn
        private String position;
        @CsvColumn
        private double salary;
        @CsvColumn
        private boolean tempWorker;

        public int getLevel() {
            return level;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public double getSalary() {
            return salary;
        }

        public void setSalary(double salary) {
            this.salary = salary;
        }

        public boolean isTempWorker() {
            return tempWorker;
        }

        public void setTempWorker(boolean tempWorker) {
            this.tempWorker = tempWorker;
        }

    }

    @Test
    public void testWriteCSVWriterObject() throws IOException {
        File file = new File("beancsv.csv");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))) {
            BeanCsv.writeHeader(csvWriter, Worker.class);
            Worker worker1 = new Worker();
            worker1.setBirthday(new Date());
            Worker worker2 = new Worker();
            BeanCsv.write(csvWriter, worker1);
            BeanCsv.write(csvWriter, worker2);
        }
        file.delete();
    }

    @Test
    public void testWriteCSVWriterListOfObject() throws IOException {
        File file = new File("beancsv.csv");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))) {
            BeanCsv.writeHeader(csvWriter, Worker.class);
            List<Worker> workers = new ArrayList<>();
            Worker worker1 = new Worker();
            worker1.setBirthday(new Date());
            Worker worker2 = new Worker();
            workers.add(worker1);
            workers.add(worker2);
            BeanCsv.write(csvWriter, workers);
        }
        file.delete();
    }

    @Test
    public void testParseBeans() throws IOException {
        File file = new File("beancsv.csv");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))) {
            BeanCsv.writeHeader(csvWriter, Worker.class);
            Worker worker1 = new Worker();
            worker1.setBirthday(new Date());
            Worker worker2 = new Worker();
            BeanCsv.write(csvWriter, worker1);
            BeanCsv.write(csvWriter, worker2);
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            List<Worker> workers = BeanCsv.parseBeans(csvReader, Worker.class, true);
            assertEquals(2, workers.size());
        }
        file.delete();
    }

    @Test
    public void testParseBeansWithMissingColumns() throws IOException {
        File file = new File("beancsv.csv");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))) {
            BeanCsv.writeHeader(csvWriter, Worker.class);
            Worker worker1 = new Worker();
            worker1.setBirthday(new Date());
            BeanCsv.write(csvWriter, worker1);
            String[] worker2 = { "worker2", "", "zhi qiang", "li" };
            csvWriter.writeNext(worker2);
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            List<Worker> workers = BeanCsv.parseBeans(csvReader, Worker.class, true);
            assertEquals(2, workers.size());
        }
        file.delete();
    }

    @Test
    public void testParseBeansWithEmptyLines() throws IOException {
        File file = new File("beancsv.csv");
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(file))) {
            BeanCsv.writeHeader(csvWriter, Worker.class);
            Worker worker1 = new Worker();
            worker1.setBirthday(new Date());
            BeanCsv.write(csvWriter, worker1);
            csvWriter.writeNext(new String[0]);
            csvWriter.writeNext(new String[0]);
            BeanCsv.write(csvWriter, new Worker());
        }
        try (CSVReader csvReader = new CSVReader(new FileReader(file))) {
            List<Worker> workers = BeanCsv.parseBeans(csvReader, Worker.class, true);
            assertEquals(2, workers.size());
        }
        file.delete();
    }

}
