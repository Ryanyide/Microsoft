package service;

import java.io.*;
import java.util.*;

public class kvservice {
	Scanner in = new Scanner(System.in);
	private Map<String, String>[] tables = new HashMap[100];//最多创建100个表
	private String[] tableName = new String[100];
	private int num = 0; //实际表数量
	private int target; //当前操作的表的索引
	//创建表
	public void create(String name) {
		tables[num] = new HashMap<String, String>();
		tableName[num] = name;
		target = num;
		num++;
	}
	
	// 操作表
	public void use(int target) {
		this.target = target;
	}
	
	public void use(String name) {
		for(int i=0; i<num; i++) {
			if(tableName[i].equals(name)) {
				target = i;
				break;
			}
		}
	}
	
	//判断是否存在
	public boolean contains(String key) {
		return tables[target].containsKey(key);
	}
	
	//增
	public void add(String key, String value) {
		tables[target].put(key, value);
	}
	
	//删
	public void delete(String key) {
		tables[target].remove(key);
	}
	
	//改
	public void change(String key, String value) {
		tables[target].put(key, value);
	}
	
	//查
	public String get(String key) {
		return tables[target].get(key);
	}
	
	//打印表
	public String show() {
		String ans = "";
		for(String key : tables[target].keySet()) {
			ans = ans + key + " " + tables[target].get(key) + " ";
		}
		return ans;
	}
	
	public String showAllTableName() {
		String anString = "";
		for(int i=0; i<num; i++) {
			anString = anString + i + " : " + tableName[i];
		}
		return anString;
	}
	
	//写入硬盘
	public void write() {
		try {
			Writer writer = new FileWriter(new File("E:/member2.txt"));
			int i=0;//表的下标
			while(i<num) {
				writer.write("table " + tableName[i] + "\r\n");;//表名
				for(String key : tables[i].keySet()) {
					writer.write(key + " " + tables[i].get(key) + "\r\n");//数据
				}
				i++;
			}
			writer.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	//读数据
	public void read() {
		try {
			FileInputStream fin = new FileInputStream("E:/member2.txt");
			InputStreamReader reader = new InputStreamReader(fin);
			BufferedReader buffReader = new BufferedReader(reader);
			String strTmp = "";
			while((strTmp = buffReader.readLine())!=null) {
				String[] a = strTmp.split(" ");
				if(a[0].contentEquals("table")) {
					create(a[1]);
				}else {
					add(a[0],a[1]);
				}
			}
			buffReader.close();
			reader.close();
			fin.close();
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	//接受命令
	public String command(String aString) {
		String ans = "success";
		String[] cmd = aString.split(" ");
		
//		for(int i=0; i<cmd.length; i++) {
//			cmd[i] = cmd[i].replaceAll(" ", "");
//		}
		
		String command = cmd[0];
		if(command.equals("create")) {
			create(cmd[1]);
		}else if(command.equals("use")) {
			use(cmd[1]);
		}else if(command.equals("add")) {
			add(cmd[1], cmd[2]);
		}else if(command.equals("contains")) {
			if(contains(cmd[1])) {
				ans = "This key exists";
			}else {
				ans = "This key does not exists";
			}
		}else if(command.contentEquals("get")) {
			ans = "key:" + cmd[1] + " value:" +get(cmd[1]);
		}else if(command.equals("delete")) {
			delete(cmd[1]);
		}else if(command.equals("show")) {
			ans = show();
		}else if(command.equals("showall")) {
			ans = showAllTableName();
		}else if(command.equals("end")) {
			write();
			//read();
			System.out.println("System end");
			return "end";
		}else {
			return "false,tryAgain";
		}
		return ans;
	}
}
