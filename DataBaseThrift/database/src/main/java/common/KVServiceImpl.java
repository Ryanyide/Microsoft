package common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import org.apache.thrift.TException;
import thrift.KVService;

public class KVServiceImpl implements KVService.Iface {
	
	private Map<String, String>[] tables = new HashMap[100];//最多创建100个表
	private String[] tableName = new String[100];
	private int num = 0; //实际表数量
	private int target; //当前操作的表的索引
	
	//创建表
	@Override
	public void create(String name) throws TException {
		// TODO Auto-generated method stub
		tables[num] = new HashMap<String, String>();
		tableName[num] = name;
		target = num;
		num++;
	}
	//使用表
	@Override
	public void usetargetbyid(int target) throws TException {
		// TODO Auto-generated method stub
		this.target = target;	
	}
	@Override
	public void usetargetbyname(String name) throws TException {
		// TODO Auto-generated method stub
		for(int i=0; i<num; i++) {
			if(tableName[i].equals(name)) {
				target = i;
				break;
			}
		}
	}
	
	//判断是否存在
	@Override
	public boolean contains(String key) throws TException {
		// TODO Auto-generated method stub
		return tables[target].containsKey(key);
	}
	@Override
	public void add(String key, String value) throws TException {
		// TODO Auto-generated method stub
		tables[target].put(key, value);
	}
	@Override
	public void remove(String key) throws TException {
		// TODO Auto-generated method stub
		tables[target].remove(key);
	}
	@Override
	public void change(String key, String value) throws TException {
		// TODO Auto-generated method stub
		tables[target].put(key, value);
	}
	@Override
	public String get(String key) throws TException {
		// TODO Auto-generated method stub
		return tables[target].get(key);
	}
	@Override
	public String show() throws TException {
		// TODO Auto-generated method stub
		String ans = "";
		for(String key : tables[target].keySet()) {
			ans = ans + key + " " + tables[target].get(key) + " ";
		}
		return ans;
	}
	@Override
	public String showAllTableName() throws TException {
		// TODO Auto-generated method stub
		String anString = "";
		for(int i=0; i<num; i++) {
			anString = anString + i + " : " + tableName[i];
		}
		return anString;
	}
	
	//写文件
	@Override
	public void write() throws TException {
		// TODO Auto-generated method stub
		try {
			Writer writer = new FileWriter(new File("E:/member3.txt"));
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
	
	//读文件
	@Override
	public void read() throws TException {
		// TODO Auto-generated method stub
		try {
			FileInputStream fin = new FileInputStream("E:/member3.txt");
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
	//处理命令
	@Override
	public String command(String aString) throws TException {
		// TODO Auto-generated method stub
		String ans = "success";
		String[] cmd = aString.split(" ");
		
//		for(int i=0; i<cmd.length; i++) {
//			cmd[i] = cmd[i].replaceAll(" ", "");
//		}
		
		String command = cmd[0];
		if(command.equals("create")) {
			create(cmd[1]);
		}else if(command.equals("usetargetbyid")) {
			usetargetbyid(Integer.parseInt(cmd[1]));
		}else if(command.equals("usetargetbyname")) {
			usetargetbyname(cmd[1]);
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
			remove(cmd[1]);
		}else if(command.equals("show")) {
			ans = show();
		}else if(command.equals("showall")) {
			ans = showAllTableName();
		}else if(command.equals("end")) {
			write();
			//read();
			System.out.println("System end");
			ans = "end";
		}else {
			return "false,tryAgain";
		}
		return ans;
	}
}
