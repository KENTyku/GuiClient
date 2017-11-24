package guiclient;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*; //для ввода вывода информации
//import java.net.*;
//import java.util.*;
import javax.swing.*;//для создания объектов gui
import java.awt.*;//для создания gui и графики
import java.awt.event.*;//обработка событий при реакции на объекты gui
import java.net.Socket;



/**
 * 
 * client chat GUI 
 *
 * @author Tveritin Yuri
 * 
 */
public class GuiClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        MyForm1 w;
        w = new MyForm1();
    }
    
}

class MyForm1 extends JFrame implements IConstants  {
    Socket socket;
    PrintWriter writer;
    BufferedReader reader;
    final JTextArea jtacenter;//многострочное текстовое поле (для отображения пришедших сообщений)
    JScrollPane jspcenter;//объект прокрутки текста JScrollPane для текстового  поля
    JTextArea jtaright;//многострочное текстовое поле JTextArea (для отображения списка Пользователей)
    JScrollPane jspright;//объект прокрутки текста JScrollPane для второго текстового поля
    final JTextArea writecenter;//многострочное текстовое поле JTextArea (для отправки сообщений)
    JScrollPane writecenterscroll;//объект прокрутки текста JScrollPane для третьего текстового поля
    JButton buttonSend;//конпка Отправить
	MyForm1() {
                
		//создаем окно 
		setTitle("KENTyku Chat");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(976, 0, 400, 700);
		
		setLayout(new BorderLayout());  //применяем компоновщик BorderLayout для располжения 2х панелей
		JPanel[] jp = new JPanel[2];
		for (int i = 0; i < jp.length; i++) {
			jp[i] = new JPanel();
			jp[i].setBackground(new Color(10 + i * 40, 130 + i * 40, 130 + i * 40));// Color(int r, int g, int b)
		}
		add(jp[0], BorderLayout.CENTER); //add panel on form
		add(jp[1], BorderLayout.SOUTH); //add panel on form
		//на первую панель кладем 
		jp[0].setLayout(new BorderLayout()); //применяя компоновщик BorderLayout к первой панели
		jtacenter = new JTextArea(); /*создаем многострочное текстовое поле (для отображения пришедших сообщений)
			JTextArea*/
		jspcenter = new JScrollPane(jtacenter);//создаем объект прокрутки текста JScrollPane куда помещаем созданное поле
		jtaright = new JTextArea(" Users "); //создаем многострочное текстовое поле JTextArea (для отображения списка Пользователей)
		jspright = new JScrollPane(jtaright);//создаем объект прокрутки текста JScrollPane куда помещаем созданное поле
		jp[0].add(jspcenter, BorderLayout.CENTER);//размещаем объект jsp на панель
		jp[0].add(jspright, BorderLayout.EAST);//размещаем объект jsp на панель
		//на вторую панель
		jp[1].setLayout(new BorderLayout());//применяем компоновщик BorderLayout на второй панели
		writecenter = new JTextArea(); //создаем многострочное текстовое поле JTextArea (для отправки сообщений)
		writecenterscroll = new JScrollPane(writecenter);//создаем объект прокрутки текста JScrollPane куда помещаем созданное поле
		buttonSend = new JButton("Send ");
		jp[1].add(writecenterscroll, BorderLayout.CENTER);//размещаем объект jsp на панель
		jp[1].add(buttonSend, BorderLayout.EAST);//размещаем объект jsp на панель
		
		
		//обработка события кнопки Send
		buttonSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
                            
//				jtacenter.append(writecenter.getText()+"\n");//добавление в главное поле чата текста из поля ввода
////ниже используется оператор try с ресурсами для автоматического закрытия файла на случай его неиспользования, чтобы предотвратить утечку ОЗУ компьютера			
//				try (BufferedWriter buffer=new BufferedWriter(new FileWriter("1.txt", true))) {//создание объекта 1.txt с именем и возможностью дозаписи (true)
//					buffer.write(writecenter.getText());//вывод информации в файл
//					buffer.newLine();//запись со следующей строки в файл					
//				}
//				catch (Exception er){
//					System.out.println ("Error");
//				}

                            if (writecenter.getText().trim().length()>0){ /*если  в поле есть
                                какой-нибудь текст то отправить его в сокет*/
                                writer.println(writecenter.getText());
                                writer.flush();
                            }
                            
				writecenter.setText(null);//очистка поля ввода
			}
		});
		
		
		//создание верхнего меню
		
		JMenuBar mainMenu = new JMenuBar();//создание меню
		JMenu mFile = new JMenu("File");//создание пункта меню
		JMenu mEdit = new JMenu("Edit");//создание пункта меню
		JMenuItem miFileNew = new JMenuItem("New");//создание подпункта меню
		JMenuItem miFileExit = new JMenuItem("Exit");//создание подпункта меню
		JMenuItem miEditCut = new JMenuItem("Add");//создание подпункта меню
		setJMenuBar(mainMenu);//установка меню mainMenu
		mainMenu.add(mFile);//добавление пункта в меню mainMenu
		mainMenu.add(mEdit);//добавление пункта в меню mainMenu
		mFile.add(miFileNew);//добавление подпункта в пункт mFile меню mainMenu
		mFile.addSeparator(); // разделительная линия
		mFile.add(miFileExit);//добавление подпункта в пункт mFile меню mainMenu
		mEdit.add(miEditCut);//добавление подпункта в пункт mEdit меню mainMenu
		
		//обработка событий в меню
		miFileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});	
		miEditCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtacenter.append("Hello \n");//добавляет в основное поле чата строку Hello в конце.
			}
		});			
		setVisible(true);
                //подключение к серверу
                Connect();



	}
        
        
        final void Connect(){            
            try {
                socket = new Socket(SERVER_ADDR, SERVER_PORT);
                writer = new PrintWriter(socket.getOutputStream());//объект для записи в поток вода-вывода( в сокет) 
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//объект для чтения из потока ввода-вывода(из сокета) 
                writer.println("Test"); // отправляем в сокет аутентификационную строку формируемую методом getLoginAndPassword()
                writer.flush();//очищаем объект
                new Thread(new ServerListener()).start();//создаем в отдельном потоке объект прослушивания сервера
    //            // цикл для отлавливания строки со значением exit и написания текста на сервер
    //            do {
    //                message = scanner.nextLine();//запрос на ввод текста от пользователя
    //                writer.println(message);//отправляем этот текст на сервер
    //                writer.flush();//очищаем поток ввода вывода
    //            } while (!message.equals(EXIT_COMMAND));
    //            socket.close();
            } catch (Exception ex) {
                jtacenter.append(ex.getMessage());
            }    
    //        System.out.println(CONNECT_CLOSED);
        }

/**      
     * ServerListener: get messages from Server
     */
    class ServerListener implements Runnable {
        String message;
        @Override
        public void run() {
            try {
                while ((message = reader.readLine()) != null) {/*цикл постоянного 
                    чтения строк из сокета сервера. И если сообщение с сервера не пустое*/
                    if (!message.equals("\0")){                        
                        /*если сообщение с сервера не = \0,
                            то печатать $, в противном случае печатать само сообщение с 
                            переносом на след строку */
                        jtacenter.append(message + "\n");                        
                    }                    
                    if (message.equals(AUTH_FAIL))/*если сообщение с сервера отказ 
                        в авторизации, то закрыть клиент*/
                        System.exit(-1); // terminate client
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}