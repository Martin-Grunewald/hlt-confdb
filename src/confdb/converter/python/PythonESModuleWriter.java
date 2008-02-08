package confdb.converter.python;

import confdb.converter.ConversionException;
import confdb.converter.ConverterEngine;
import confdb.converter.IESModuleWriter;
import confdb.data.ESModuleInstance;

public class PythonESModuleWriter extends PythonInstanceWriter implements IESModuleWriter 
{
    public String toString(ESModuleInstance esmodule,ConverterEngine converterEngine, String indent) throws ConversionException 
    {
	return toString("ESModule",esmodule,converterEngine, indent);
    }
    
}
