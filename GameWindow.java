package com.company;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;

public class GameWindow extends JFrame{ //Наследуемся от класса JFrame

//Обьявляем переменые общие
    private static GameWindow game_window;// переменная в крт. хранится обьект окна
    public static long last_frame_time;//переменная дляподсчета времени
    private static Image Background;//обьявляем класс работы с картинкой
    private static Image GameOver;
    private static Image Drop;
    private static float Drop_left = 200;//Хранит координату х верхнего левого угла
    private static float Drop_top = -100;//Хранит координату у верхнего левого угла
    private static float Drop_v = 200;//скорость капли
    private static int score; //для подсчета очков
//работа с окном
    public static void main(String[] args) throws IOException {//выносим исключния
        Background = ImageIO.read(GameWindow.class.getResourceAsStream("background.png"));//загружаем картинки
        GameOver = ImageIO.read(GameWindow.class.getResourceAsStream("game_over.png"));
        Drop = ImageIO.read(GameWindow.class.getResourceAsStream("drop.png"));
	    game_window = new GameWindow();//обьект окна на который ссылается переменная
        game_window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//Завершение программы после закрытия окна
        game_window.setLocation(200,100);//Положение окна
        game_window.setSize(906,478);//Размер окна
        game_window.setResizable(false); //Запрет на увеличение окна
        last_frame_time = System.nanoTime();//возвращает текущее время в наносекундах
        GameField game_field = new GameField(); //Создали обьекс класса GameField

        game_field.addMouseListener(new MouseAdapter() { //команда при нажатии кнопки
            @Override
            public void mousePressed(MouseEvent e) {
                int x = e.getX();//получаем координаты щелчка мыши
                int y = e.getY();
                float Drop_right = Drop_left + Drop.getWidth(null); //подсчет правой  границы
                float Drop_bottom = Drop_top + Drop.getHeight(null);//подсчет нижней границы
                boolean is_drop = x >= Drop_left && x <= Drop_right && y >= Drop_top && y <= Drop_bottom; //Проверка попадения щелчка по капле
                if (is_drop)  {//если попала по каплеб то откидываем её за границы окна
                    Drop_top = -100;
                    Drop_left = (int) (Math.random() * (game_field.getWidth() - Drop.getWidth(null)));//перенос в рандомное место
                    Drop_v = Drop_v + 20;//увеличиваем скорость капли
                    score ++;
                    game_window.setTitle("Score : " + score);
                }
            }
        });
        game_window.add(game_field);//добавляем GameField в окно
        game_window.setVisible(true);//Видимость окна

    }
    //рисование в окне

    private static void onRepaint (Graphics g){ //Метод длярисования
        //g.fillOval(10,10,200,500);// Рисуем овал
        long current_time = System.nanoTime();//получение текущего времени
        float delta_time = (current_time - last_frame_time) * 0.000000001f;//подсчет разницы во времени и перевод наносекунд в секунду
        last_frame_time = current_time;//время предыдущего кадра=текущее время
        Drop_top = Drop_top + Drop_v * delta_time;//перемещение капли

        g.drawImage(Background, 0, 0, null);//рисуем фон
        g.drawImage(Drop, (int)Drop_left,(int)Drop_top, null);
        if (Drop_top > game_window.getHeight()) g.drawImage(GameOver, 280, 120, null); //если капля вылетела за границу экранаб то выводим GameOver


    }
    private static class GameField extends JPanel{

        @Override //Динамическое замещение методов
        protected void paintComponent(Graphics g){ //переопределяем метод paintComponent
            super.paintComponent(g); //доступ к paintComponent в классе JPanel
            onRepaint(g);
            repaint();//для частого вызывания метода paintComponent
        }

    }
}
