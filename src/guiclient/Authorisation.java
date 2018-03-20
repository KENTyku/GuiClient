/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package guiclient;

import java.awt.BorderLayout;
import java.awt.Color;
import static java.awt.Component.CENTER_ALIGNMENT;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

/**
 *
 * @author user
 
Окно авторизации
*/
public class Authorisation extends JFrame implements IConstants{
    JTextField servername;//сервер чата
    JTextField login;//логин пользователя
    JTextField psw;//пароль пользователя
    JButton connect;//авторизоваться с выбранными настройками 
    
    Authorisation(){
        //создаем окно 
		setTitle("Logining");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(500, 500, 400, 400);
		//применяем компоновщик BorderLayout для располжения 2х панелей
		setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS));  
		JPanel[] jp = new JPanel[1];
		for (int i = 0; i < jp.length; i++) {
			jp[i] = new JPanel();
			jp[i].setBackground(new Color(10 + i * 40, 130 + i * 40, 130 + i * 40));// Color(int r, int g, int b)
		}
		add(jp[0], BorderLayout.CENTER); //add panel on form		
		//на первую панель кладем 
		jp[0].setLayout(new BoxLayout(getContentPane(),BoxLayout.Y_AXIS)); //применяя компоновщик BoxLayout к первой панели
		servername = new JTextField("Server name"); /*создаем текстовое поле (для 
                ввода адреса сервера)*/
		login = new JTextField("Your login");//создаем текстовое поле для ввода логина
                psw = new JTextField("Your password");//создаем текстовое поле для ввода пароля
		connect = new JButton("Connect");
                servername.setAlignmentX(CENTER_ALIGNMENT);
                login.setAlignmentX(CENTER_ALIGNMENT);
                psw.setAlignmentX(CENTER_ALIGNMENT);
                connect.setAlignmentX(CENTER_ALIGNMENT);
                add(servername);
                add(login);
                add(psw);
                add(connect);
//                setVisible(true);
                //обработка события кнопки Send
                
		connect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            System.exit(0);
                        }
                });        
//                setVisible(true);
    }
}
