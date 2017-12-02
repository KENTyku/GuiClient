package guiclient;

/*
 * To change this license header, choose License Headers in Project Properties.
Баги:
1. После выключения сервера клиенты сами уже не могут подключиться к вновь 
запущенному серверу.

 */

import java.io.*; //для ввода вывода информации
import javax.swing.*;//для создания объектов gui
import java.awt.*;//для создания gui и графики
import java.awt.event.*;//обработка событий при реакции на объекты gui
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/** 
 * client chat GUI
 * @author Tveritin Yuri 
 */
public class GuiClient {   
    public static void main(String[] args) {
        MyGui w;
        w = new MyGui();
    }    
}

/*Графический интерфейс*/

class MyGui extends JFrame implements IConstants  {
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
    
    /*конструктор графического интерфейса*/
    
	MyGui() {
                
		//создаем окно 
		setTitle("KENTyku Chat");
		setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		setBounds(976, 0, 400, 700);
		//применяем компоновщик BorderLayout для располжения 2х панелей
		setLayout(new BorderLayout());  
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
		jtaright = new JTextArea("    Users   "); //создаем многострочное текстовое поле JTextArea (для отображения списка Пользователей)
		jspright = new JScrollPane(jtaright);//создаем объект прокрутки текста JScrollPane куда помещаем созданное поле
		jp[0].add(jspcenter, BorderLayout.CENTER);//размещаем объект jsp на панель
		jp[0].add(jspright, BorderLayout.EAST);//размещаем объект jsp на панель
		//на вторую панель
		jp[1].setLayout(new BorderLayout());//применяем компоновщик BorderLayout на второй панели
		writecenter = new JTextArea(); //создаем многострочное текстовое поле JTextArea (для отправки сообщений)
		writecenterscroll = new JScrollPane(writecenter);//создаем объект прокрутки текста JScrollPane куда помещаем созданное поле
		buttonSend = new JButton("SEND ");
		jp[1].add(writecenterscroll, BorderLayout.CENTER);//размещаем объект jsp на панель
		jp[1].add(buttonSend, BorderLayout.EAST);//размещаем объект jsp на панель
		
                //создание верхнего меню
		
		JMenuBar mainMenu = new JMenuBar();//создание меню
		JMenu mFile = new JMenu("Connection");//создание пункта меню
		JMenu mEdit = new JMenu("Edit");//создание пункта меню
                JMenuItem miFileRegistration = new JMenuItem("Registration");//создание подпункта меню
		JMenuItem miFileReconnect = new JMenuItem("Reconnect");//создание подпункта меню
		JMenuItem miFileExit = new JMenuItem("Exit");//создание подпункта меню
		JMenuItem miEditCut = new JMenuItem("Add");//создание подпункта меню
		setJMenuBar(mainMenu);//установка меню mainMenu
		mainMenu.add(mFile);//добавление пункта в меню mainMenu
		mainMenu.add(mEdit);//добавление пункта в меню mainMenu
                mFile.add(miFileRegistration);//добавление подпункта в пункт mFile меню mainMenu
                mFile.addSeparator(); // разделительная линия
		mFile.add(miFileReconnect);//добавление подпункта в пункт mFile меню mainMenu
		mFile.addSeparator(); // разделительная линия
		mFile.add(miFileExit);//добавление подпункта в пункт mFile меню mainMenu
		mEdit.add(miEditCut);//добавление подпункта в пункт mEdit меню mainMenu
		
                
                /*
                *
                *Обработчики событий графического интерфейса
                *
                */
                
		//обработка события кнопки Send
                
		buttonSend.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {                            
                           //отправка сообщения на сервер                            
                            if (writecenter.getText().trim().length()>0){ /*если  в поле есть
                                какой-нибудь текст то отправить его в сокет*/
                                writer.println(writecenter.getText());
                                writer.flush();
                            }
                            //отправка сообщения в текстовый файл(история исходящих сообщений) 
                            /*
                             * ниже используется оператор try с ресурсами для 
                             *автоматического закрытия файла на случай его 
                             *неиспользования, чтобы предотвратить утечку ОЗУ 
                             *компьютера
                             */			
                            try (BufferedWriter buffer=new BufferedWriter(new FileWriter("1.txt", true))) {//создание объекта 1.txt с именем и возможностью дозаписи (true)
                                buffer.write(writecenter.getText());//вывод информации в файл
                                buffer.newLine();//запись со следующей строки в файл					
                            }
                            catch (Exception er){
                                    System.out.println ("Error");
                            }
                             // цикл для отлавливания строки со значением exit  и выходом из приложения
                            if (writecenter.getText().equals(EXIT_COMMAND)) {
                                try {
                                    socket.close();//закрытие сетевых подключений
                                    System.exit(0);//выход из приложения
                                } catch (IOException ex) {
                                    Logger.getLogger(MyGui.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            }
                            //очистка поля ввода отправки сообщений
                            writecenter.setText(null);
			}
		});	
		
		//обработка событий в меню
                
                //пункт Registarion
                miFileRegistration.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtacenter.append("\n Registration \n");//добавляет в основное поле чата строку 
                                registration();
			}
		});		
                
                //пункт Reconnect
                miFileReconnect.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jtacenter.append("\n Reconnect \n");//добавляет в основное поле чата строку 
                                Connect();
			}
		});		
                
                //пункт Exit
		miFileExit.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
                
		//пункт Add . Вызов диалогового окна
                miEditCut.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {				
                                String s=JOptionPane.showInputDialog(MyGui.this, new String[] {"Неверно введен пароль!","Повторите пароль :"}, "Авторизация", JOptionPane.WARNING_MESSAGE);                                
                                jtacenter.append("\n"+s+"\n");

			}
		});			
		setVisible(true);
                
                //
                //подключение к серверу
                //
          
                Connect();
//                authorisation();
	}
        
        
        /*
        *метод подключения клиента к серверу
        *
        *
        */
        
        final void Connect(){            
            try {
                socket = new Socket(SERVER_ADDR, SERVER_PORT);
                writer = new PrintWriter(socket.getOutputStream());//объект для записи в поток вода-вывода( в сокет) 
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));//объект для чтения из потока ввода-вывода(из сокета) 
                
                writer.println(authorisation()); // отправляем в сокет аутентификационную строку формируемую методом authorisation()
                writer.flush();//очищаем объект
//                jtacenter.append("\n Write command auth and enter  login and password \n and press SEND \n"); 
                new Thread(new ServerListener()).start();//создаем в отдельном потоке объект прослушивания сервера
                
    //           
            } catch (Exception ex) {
                jtacenter.append(ex.getMessage());
            }    
    //        System.out.println(CONNECT_CLOSED);
        }
        /*
        авторизация на сервере
        */
        String authorisation(){
            String l=JOptionPane.showInputDialog(MyGui.this, new String[] {"Для подключения к сетевому чату","введите ваш логин"}, "Авторизация", JOptionPane.WARNING_MESSAGE);                                
//            jtacenter.append("\n"+l+"\n");
            String p=JOptionPane.showInputDialog(MyGui.this, new String[] {"Для подключения к сетевому чату","введите ваш пароль"}, "Авторизация", JOptionPane.WARNING_MESSAGE);                                
//            jtacenter.append("\n"+p+"\n");
            return("auth "+l+" "+p);
        }
        /*
        запрос на регистрацию 
        если не авторизован
        */
        String registration(){
            String newlogin=JOptionPane.showInputDialog(MyGui.this, new String[] {"Регистрация нового пользователя","Придумайте логин"}, "Регистрация нового пользователя", JOptionPane.WARNING_MESSAGE);                                
            String newpass=JOptionPane.showInputDialog(MyGui.this, new String[] {"Регистрация нового пользователя","Придумайте пароль"}, "Регистрация нового пользователя", JOptionPane.WARNING_MESSAGE);
            String email=JOptionPane.showInputDialog(MyGui.this, new String[] {"Регистрация нового пользователя","Введите ваш email"}, "Регистрация нового пользователя", JOptionPane.WARNING_MESSAGE);
            String birthday=JOptionPane.showInputDialog(MyGui.this, new String[] {"Регистрация нового пользователя","Введите ваш день рождения(число и месяц)"}, "Регистрация нового пользователя", JOptionPane.WARNING_MESSAGE);
            return("/reg"+newlogin+";"+newpass+";"+email+";"+birthday);
        }
        
        
           

    /**      
     * Класс прослушивания сервера
     */
    class ServerListener implements Runnable {
        String message;//вспомогательная переменная для хранения сообщения от сервера
        @Override
        public void run() {
            try {
                //непрерывное чтение строк от сервера в цикле
                while ((message = reader.readLine()) != null) {/*цикл постоянного 
                    чтения строк из сокета сервера. И если сообщение от сервера не пустое*/
                    if (message.startsWith("/userlistadd")){//находим признак строки списка пользоватлей
                        Integer index=message.lastIndexOf("/userlistend"); //определяем номер симова конца троки списка пользователей                                                                
                        String[] userlist = message.substring(12, index).split(";");//формируем из строки списка пользоватлей массив с пользователями
                        jtaright.setText("Users:");//обновляем поле списка пользователей(перезаписываем его)
                        for (String user : userlist) {
                            jtaright.append("\n"+user);
                        }
                        //печатаем остаток того, что пришло после списка пользователей из сокета
                        jtacenter.append(message.substring(index+12)+"\n");//вырезаем из строки всё что справа от символа с учетом поправки на длину 'userlistend'    
                    }
                   else
                    if (!message.equals("\0")){                        
                        /*если сообщение с сервера не = \0,
                            то печатать $, в противном случае печатать само сообщение с 
                            переносом на след строку */
                        jtacenter.append(message + "\n");                        
                    }                    
//                    if (message.equals(AUTH_FAIL))/*если сообщение с сервера отказ 
//                        в авторизации, то закрыть клиент*/
//                        System.exit(-1); // terminate client
                }
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
        }
    }
}

