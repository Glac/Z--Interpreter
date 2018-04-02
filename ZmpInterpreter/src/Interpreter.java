import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.concurrent.ConcurrentHashMap;

public class Interpreter {

	public static ConcurrentHashMap<String, Object> variables = new ConcurrentHashMap<String, Object>();

	public static void main(String[] args) throws FileNotFoundException {

		Scanner input = new Scanner(System.in);
		System.out.print("Please enter the file you want to run: ");
		String fileName = input.next();
		input = new Scanner(new File(fileName));

		while (input.hasNext()) {
			String command_line = input.nextLine();
			excuteCommand(command_line);
		}

	}
	
	public static void excuteCommand(String command_line){
		String[] commands = command_line.split(" ");
		String variable_name, ObjectName;

		// Interpret the "=" command in the ZPM file
		if (commands[1].equals("=")) {
			variable_name = commands[0];
			ObjectName = commands[2];

			if (ObjectName.contains("\"")) {
				variables.put(variable_name, ObjectName);
			} else if (variables.get(ObjectName) != null) {
				variables.put(variable_name, (int) variables.get(ObjectName));
			} else {
				variables.put(variable_name, Integer.parseInt(ObjectName));
			}
		}

		// Interpret the "+=" command in the ZPM file
		else if (commands[1].equals("+=")) {
			variable_name = commands[0];
			ObjectName = commands[2];

			if (variables.get(ObjectName) != null) {
				if (variables.get(ObjectName) instanceof Integer) {
					int value = (int) variables.get(ObjectName) + (int) variables.get(variable_name);
					variables.put(variable_name, value);
				}

				if (variables.get(ObjectName) instanceof String) {
					String value = (String) variables.get(ObjectName) + (String) variables.get(variable_name);
					variables.put(variable_name, value);
				}
			} else {
				if (variables.get(variable_name) == null)
					System.out.println("Error happened! Variable didn't declared ! ");
				else {
					int value = Integer.parseInt(ObjectName) + (int) variables.get(variable_name);
					variables.put(variable_name, value);
				}
			}
		}

		// Interpret the "*=" command in the ZPM file
		else if (commands[1].equals("*=")) {
			variable_name = commands[0];
			ObjectName = commands[2];
			int value;
			if ((variables.get(variable_name) != null) && (variables.get(variable_name) instanceof Integer)) {
				if ((variables.get(ObjectName) != null) && (variables.get(ObjectName) instanceof Integer))
					value = (int) variables.get(ObjectName);
				else
					value = Integer.parseInt(ObjectName);
				variables.put(variable_name, value * (int) variables.get(variable_name));
			} else
				System.out.println("Error happnened! Not the correct data type to calculate !");
		}

		// Interpret the "-=" command in the ZPM file
		else if (commands[1].equals("-=")) {
			variable_name = commands[0];
			ObjectName = commands[2];
			int value = 0;
			if ((variables.get(variable_name) != null) && (variables.get(variable_name) instanceof Integer)) {
				if ((variables.get(ObjectName) != null) && (variables.get(ObjectName) instanceof Integer))
					value = (int) variables.get(ObjectName);
				else
					value = Integer.parseInt(ObjectName);
				variables.put(variable_name, (int) variables.get(variable_name) - value);
			} else
				System.out.println("Error happnened! Not the correct data type to calculate !");
		}

		// Interpret the "PRINT" command in the ZPM file
		else if (commands[0].equals("PRINT")) {
			ObjectName = commands[1];
			if ((variables.get(ObjectName) != null)) {
				if (variables.get(ObjectName) instanceof String) {
					String strValue = (String) variables.get(ObjectName);
					System.out.println(ObjectName + "=" + strValue.replace("\"", ""));
				} else if (variables.get(ObjectName) instanceof Integer) {
					System.out.println(ObjectName + "=" + (int) variables.get(ObjectName));
				} else
					System.out.println("Error happnened! Not the correct data type !");
			}
		}

		// Interpret the loop in the ZPM file
		else if (commands[0].equals("FOR")) {
			int loop_times = Integer.parseInt(commands[1]);
			int nest_of_loop = 0;
			int count = -1;
			int index_of_for = -1;
			String newline = "";

			command_line = command_line.substring(command_line.indexOf(" ", 4)).trim();
			for(int i = 0; i < loop_times; i++ ){
				String[] statements = command_line.split(";");

				for(String statement: statements){
					/*
					if(statement.contains("FOR") && !statement.equals("ENDFOR") ){	
						
						System.out.println(statements[statements.length-1]);

						for(int j = statements.length-2; j>0; j --){
							if(statements[j].trim().equals("ENDFOR")){	
								count = j;
								System.out.println(count);
							}
						}
						for(int a = 0; a < statements.length-1;a++){
							if(statements[a].trim().contains("FOR")){	
								index_of_for = a;
							}
						}
						
						for(int b = index_of_for; b < count+1; b++ ){
							newline += statements[b] + " ";
						}
						
						System.out.println(newline);
						excuteCommand(newline.trim());
					}
					*/
					if(!statement.trim().equals("ENDFOR"))
						excuteCommand(statement.trim());
				}
			}
		}

		else
			System.err.println("RUNTIME ERROR: wrong command ");
	} 
}
