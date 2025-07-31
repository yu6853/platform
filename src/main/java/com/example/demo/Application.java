package com.example.demo;// Application.java - 主程序入口
import com.example.demo.controller.SystemController;

public class Application {
    public static void main(String[] args) {
        SystemController controller = new SystemController();
        controller.start();
    }
}
