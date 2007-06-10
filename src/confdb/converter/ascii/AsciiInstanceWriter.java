package confdb.converter.ascii;

import confdb.converter.Converter;
import confdb.converter.IParameterWriter;
import confdb.data.Instance;
import confdb.data.Parameter;

public class AsciiInstanceWriter {

	private static final String indent = "  ";

	private IParameterWriter parameterWriter = null;
	
	protected String toString( String type, Instance instance, Converter converter ) 
	{
		if ( parameterWriter == null )
			parameterWriter = converter.getParameterWriter();
		
		String str = indent + type + " = " + instance.name() + " {";
		if ( instance.parameterCount() == 0 )
			return str + "}" + converter.getNewline();
			
		str += converter.getNewline();
		for ( int i = 0; i < instance.parameterCount(); i++ )
		{
			Parameter parameter = instance.parameter(i);
			str += parameterWriter.toString( parameter, converter, indent + "  " );
		}
		str += indent + "}" + converter.getNewline();
		return str;
	}


	
}