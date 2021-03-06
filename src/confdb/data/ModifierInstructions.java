package confdb.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

/**
 * ModifierInstructions
 * --------------------
 * @author Philipp Schieferdecker
 *
 * Instructions for the ConfigurationModifier how to filter/manipulate
 * its master configuration.
 */
public class ModifierInstructions {
	//
	// member data
	//

	/** global PSets */
	private boolean filterAllPSets = false;
	private ArrayList<String> psetBlackList = new ArrayList<String>();
	private ArrayList<String> psetWhiteList = new ArrayList<String>();

	/** EDSources */
	private boolean filterAllEDSources = false;
	private ArrayList<String> edsourceBlackList = new ArrayList<String>();
	private ArrayList<String> edsourceWhiteList = new ArrayList<String>();

	/** ESSources */
	private boolean filterAllESSources = false;
	private ArrayList<String> essourceBlackList = new ArrayList<String>();
	private ArrayList<String> essourceWhiteList = new ArrayList<String>();

	/** ESModules */
	private boolean filterAllESModules = false;
	private ArrayList<String> esmoduleBlackList = new ArrayList<String>();
	private ArrayList<String> esmoduleWhiteList = new ArrayList<String>();

	/** Services */
	private boolean filterAllServices = false;
	private ArrayList<String> serviceBlackList = new ArrayList<String>();
	private ArrayList<String> serviceWhiteList = new ArrayList<String>();

	/** Paths */
	private boolean filterAllPaths = false;
	private boolean filterAllOutputModules = false;
	private ArrayList<String> pathBlackList = new ArrayList<String>();
	private ArrayList<String> pathWhiteList = new ArrayList<String>();

	/** Datasets */
	private ArrayList<String> datasetBlackList = new ArrayList<String>();
	private ArrayList<String> datasetWhiteList = new ArrayList<String>();

	/** Streams */
	private ArrayList<String> streamBlackList = new ArrayList<String>();
	private ArrayList<String> streamWhiteList = new ArrayList<String>();

	/** sequences requested regardless of being referenced in requested paths */
	private ArrayList<String> requestedSequences = new ArrayList<String>();

	/** sequences to be properly referenced but *not* defined */
	private boolean undefineAllSequences = false;
	private ArrayList<String> undefinedSequences = new ArrayList<String>();

	/** modules reqested regardless of being referenced in requested path */
	private ArrayList<String> requestedModules = new ArrayList<String>();
	private ArrayList<String> requestedOutputs = new ArrayList<String>();

	/** modules to be properly referenced but *not* defined */
	private boolean undefineAllModules = false;
	private ArrayList<String> undefinedModules = new ArrayList<String>();

	/** contents, streams, & datasets reqested */
	private ArrayList<String> requestedContents = new ArrayList<String>();
	private ArrayList<String> requestedStreams = new ArrayList<String>();
	private ArrayList<String> requestedDatasets = new ArrayList<String>();

	/** blocks to be defined, regardless of the instance being filtered! */
	private ArrayList<String> blocks = new ArrayList<String>();

	/** template for the EDSource to be substituted (if any) */
	private EDSourceTemplate edsourceT = null;
	private String edsourceFileNames = new String();

	/** template for the OutputModule to be substituted (if any) */
	private ModuleTemplate outputT = null;

	//
	// construction
	//

	/** standard constructor */
	public ModifierInstructions() {
	}

	//
	// member functions
	//

	/** interpret arguments stored in a HashMap */
	public void interpretArgs(HashMap<String, String> args) throws DataException {
		String value;

		value = args.remove("cff");
		if (value != null) {
			filterAllEDSources(true);
			args.remove("input");
			// filterAllOutputModules(true);
			// args.remove("output");
		}
		value = args.remove("nopsets");
		if (value != null)
			filterAllPSets(true);
		value = args.remove("noedsources");
		if (value != null) {
			filterAllEDSources(true);
			args.remove("input");
		}
		value = args.remove("noes");
		if (value != null) {
			filterAllESSources(true);
			filterAllESModules(true);
		} else {
			value = args.remove("noessources");
			if (value != null)
				filterAllESSources(true);
			value = args.remove("noesmodules");
			if (value != null)
				filterAllESModules(true);
		}
		value = args.remove("noservices");
		if (value != null)
			filterAllServices(true);
		value = args.remove("nopaths");
		if (value != null)
			filterAllPaths(true);
		value = args.remove("nosequences");
		if (value != null)
			undefineAllSequences();
		value = args.remove("nomodules");
		if (value != null)
			undefineAllModules();
		value = args.remove("nooutput");
		if (value != null) {
			filterAllOutputModules(true);
			args.remove("output");
		}

		value = args.remove("psets");
		if (value != null) {
			String[] psetNames = value.split(",");
			for (String s : psetNames) {
				if (s.startsWith("-"))
					insertPSetIntoBlackList(s.substring(1));
				else
					insertPSetIntoWhiteList(s);
			}
		}

		value = args.remove("edsources");
		if (value != null) {
			String[] edsourceNames = value.split(",");
			for (String s : edsourceNames) {
				if (s.startsWith("-"))
					insertEDSourceIntoBlackList(s.substring(1));
				else
					insertEDSourceIntoWhiteList(s);
			}
		}

		value = args.remove("essources");
		if (value != null) {
			String[] essourceNames = value.split(",");
			for (String s : essourceNames) {
				if (s.startsWith("-"))
					insertESSourceIntoBlackList(s.substring(1));
				else
					insertESSourceIntoWhiteList(s);
			}
		}

		value = args.remove("esmodules");
		if (value != null) {
			String[] esmoduleNames = value.split(",");
			for (String s : esmoduleNames) {
				if (s.startsWith("-"))
					insertESModuleIntoBlackList(s.substring(1));
				else
					insertESModuleIntoWhiteList(s);
			}
		}

		value = args.remove("services");
		if (value != null) {
			String[] serviceNames = value.split(",");
			for (String s : serviceNames) {
				if (s.startsWith("-"))
					insertServiceIntoBlackList(s.substring(1));
				else
					insertServiceIntoWhiteList(s);
			}
		}

		value = args.remove("paths");
		if (value != null) {
			String[] pathNames = value.split(",");
			for (String s : pathNames) {
				if (s.startsWith("-"))
					insertPathIntoBlackList(s.substring(1));
				else
					insertPathIntoWhiteList(s);
			}
		}

		value = args.remove("datasets");
		if (value != null) {
			String[] datasets = value.split(",");
			for (String s : datasets) {
				if (s.startsWith("-"))
					insertDatasetIntoBlackList(s.substring(1));
				else
					insertDatasetIntoWhiteList(s);
			}
		}

		value = args.remove("streams");
		if (value != null) {
			String[] streams = value.split(",");
			for (String s : streams) {
				if (s.startsWith("-"))
					insertStreamIntoBlackList(s.substring(1));
				else
					insertStreamIntoWhiteList(s);
			}
		}

		value = args.remove("sequences");
		if (value != null) {
			String[] sequenceNames = value.split(",");
			for (String s : sequenceNames) {
				if (s.startsWith("-"))
					undefineSequence(s.substring(1));
				else
					requestSequence(s);
			}
		}

		value = args.remove("modules");
		if (value != null) {
			String[] moduleNames = value.split(",");
			for (String s : moduleNames) {
				if (s.startsWith("-"))
					undefineModule(s.substring(1));
				else
					requestModule(s);
			}
		}

		value = args.remove("input");
		if (value != null) {
			if (value.indexOf(",") < 0 && value.endsWith(".list")) {
				BufferedReader inputStream = null;
				String listFileName = value;
				try {
					inputStream = new BufferedReader(new FileReader(listFileName));
					value = "";
					String fileName;
					while ((fileName = inputStream.readLine()) != null) {
						if (value.length() > 0)
							value += ",";
						value += fileName;
					}
				} catch (IOException e) {
					System.err.println("Error parsing '" + listFileName + "':" + e.getMessage());
				} finally {
					if (inputStream != null)
						try {
							inputStream.close();
						} catch (IOException e) {
						}
				}
			}
			insertPoolSource(value);
		}

		value = args.remove("output");
		if (value != null) {
			insertPoolOutputModule(value);
		}

		value = args.remove("blocks");
		if (value != null) {
			String[] tmp = value.split(",");
			for (String s : tmp)
				blocks.add(s);
		}

		if (args.size() > 0)
			throw new DataException("ModifierInstructions.interpretArgs ERROR: " + "invalid arguments detected.");
	}

	/** interpret a search string */

	public boolean isMatch(String source, String search, int alg) {
		switch (alg) {
		case 1:
			return source.contains(search);
		case 2:
			return source.matches(search);
		case 3:
			return source.startsWith(search);
		default:
			return false;
		}
	}

	public void interpretSearchString(String search, String mode, IConfiguration config) {
		int alg = 0;
		int obj = 0;

		String[] options = mode.split(":");

		if (options[0].equals("contains"))
			alg = 1;
		else if (options[0].equals("matches"))
			alg = 2;
		else if (options[0].equals("startsWith"))
			alg = 3;
		else
			return;

		if (options[1].equals("matchNames"))
			obj = 1;
		else if (options[1].equals("matchTypes"))
			obj = 2;
		else if (options[1].equals("matchValues"))
			obj = 3;
		else
			return;

		if (alg == 2)
			search = search.replace("*", ".*").replace("?", ".");
		System.err.println("Search: mode/pattern: " + mode + " / " + search);

		String name = null;
		String Name = null;
		String Type = null;

		Iterator<PSetParameter> itPSet = config.psetIterator();
		while (itPSet.hasNext()) {
			GlobalPSetContainer pset = new GlobalPSetContainer();
			pset.addParameter(itPSet.next().clone(pset));
			if (obj == 3 && pset.findParameters(null, null, search, alg).length > 0) {
				psetWhiteList.add(pset.parameter(0).name());
			} else {
				name = (obj == 1) ? pset.parameter(0).name() : "PSet";
				if (isMatch(name, search, alg)) {
					psetWhiteList.add(pset.parameter(0).name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (pset.findParameters(Name, Type, null, alg).length > 0)
						psetWhiteList.add(pset.parameter(0).name());
				}
			}
		}
		if (psetWhiteList.size() == 0)
			filterAllPSets(true);

		Iterator<EDSourceInstance> itEDS = config.edsourceIterator();
		while (itEDS.hasNext()) {
			EDSourceInstance eds = itEDS.next();
			if (obj == 3 && eds.findParameters(null, null, search, alg).length > 0) {
				edsourceWhiteList.add(eds.name());
			} else {
				name = (obj == 1) ? eds.name() : eds.template().name();
				if (isMatch(name, search, alg)) {
					edsourceWhiteList.add(eds.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (eds.findParameters(Name, Type, null, alg).length > 0)
						edsourceWhiteList.add(eds.name());
				}
			}
		}
		if (edsourceWhiteList.size() == 0)
			filterAllEDSources(true);

		Iterator<ESSourceInstance> itESS = config.essourceIterator();
		while (itESS.hasNext()) {
			ESSourceInstance ess = itESS.next();
			if (obj == 3 && ess.findParameters(null, null, search, alg).length > 0) {
				essourceWhiteList.add(ess.name());
			} else {
				name = (obj == 1) ? ess.name() : ess.template().name();
				if (isMatch(name, search, alg)) {
					essourceWhiteList.add(ess.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (ess.findParameters(Name, Type, null, alg).length > 0)
						essourceWhiteList.add(ess.name());
				}
			}
		}
		if (essourceWhiteList.size() == 0)
			filterAllESSources(true);

		Iterator<ESModuleInstance> itESM = config.esmoduleIterator();
		while (itESM.hasNext()) {
			ESModuleInstance esm = itESM.next();
			if (obj == 3 && esm.findParameters(null, null, search, alg).length > 0) {
				esmoduleWhiteList.add(esm.name());
			} else {
				name = (obj == 1) ? esm.name() : esm.template().name();
				if (isMatch(name, search, alg)) {
					esmoduleWhiteList.add(esm.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (esm.findParameters(Name, Type, null, alg).length > 0)
						esmoduleWhiteList.add(esm.name());
				}
			}
		}
		if (esmoduleWhiteList.size() == 0)
			filterAllESModules(true);

		Iterator<ServiceInstance> itSvc = config.serviceIterator();
		while (itSvc.hasNext()) {
			ServiceInstance svc = itSvc.next();
			if (obj == 3 && svc.findParameters(null, null, search, alg).length > 0) {
				serviceWhiteList.add(svc.name());
			} else {
				name = (obj == 1) ? svc.name() : svc.template().name();
				if (isMatch(name, search, alg)) {
					serviceWhiteList.add(svc.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (svc.findParameters(Name, Type, null, alg).length > 0)
						serviceWhiteList.add(svc.name());
				}
			}
		}
		if (serviceWhiteList.size() == 0)
			filterAllServices(true);

		Iterator<Path> itP = config.pathIterator();
		while (itP.hasNext()) {
			name = itP.next().name();
			if (isMatch(name, search, alg))
				pathWhiteList.add(name);
		}
		if (pathWhiteList.size() == 0)
			filterAllPaths(true);

		Iterator<Sequence> itS = config.sequenceIterator();
		while (itS.hasNext()) {
			name = itS.next().name();
			if (isMatch(name, search, alg))
				requestSequence(name);
		}

		Iterator<ModuleInstance> itM = config.moduleIterator();
		while (itM.hasNext()) {
			ModuleInstance module = itM.next();
			if (obj == 3 && module.findParameters(null, null, search, alg).length > 0) {
				requestModule(module.name());
			} else {
				name = (obj == 1) ? module.name() : module.template().name();
				if (isMatch(name, search, alg)) {
					requestModule(module.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (module.findParameters(Name, Type, null, alg).length > 0)
						requestModule(module.name());
				}
			}
		}

		Iterator<OutputModule> itOM = config.outputIterator();
		while (itOM.hasNext()) {
			OutputModule output = itOM.next();
			if (obj == 3 && output.findParameters(null, null, search, alg).length > 0) {
				requestOutput(output.name());
			} else {
				name = (obj == 1) ? output.name() : output.className();
				if (isMatch(name, search, alg)) {
					requestOutput(output.name());
				} else {
					Name = (obj == 1) ? search : null;
					Type = (obj == 1) ? null : search;
					if (output.findParameters(Name, Type, null, alg).length > 0)
						requestOutput(output.name());
				}
			}
		}

		Iterator<EventContent> itEC = config.contentIterator();
		while (itEC.hasNext()) {
			EventContent content = itEC.next();
			name = content.name();
			if (isMatch(name, search, alg))
				requestContent(name);
		}

		Iterator<Stream> itST = config.streamIterator();
		while (itST.hasNext()) {
			Stream stream = itST.next();
			name = stream.name();
			if (isMatch(name, search, alg))
				requestStream(name);
		}

		Iterator<PrimaryDataset> itPD = config.datasetIterator();
		while (itPD.hasNext()) {
			PrimaryDataset dataset = itPD.next();
			name = dataset.name();
			if (isMatch(name, search, alg))
				requestDataset(name);
		}

	}

	/** resolve white-lists based on a given configuration */
	public boolean resolve(IConfiguration config) {
		// stream filtering
		if (streamWhiteList.size() > 0 && streamBlackList.size() > 0) {
			System.err.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for streams.");
			return false;
		} else if (streamWhiteList.size() == 0 && streamBlackList.size() == 0) {
			Iterator<Stream> itStream = config.streamIterator();
			while (itStream.hasNext())
				requestedStreams.add(itStream.next().name());
		} else
			applyStreamFiltering(config);

		// dataset filtering
		if (datasetWhiteList.size() > 0 && datasetBlackList.size() > 0) {
			System.err.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for datasets.");
			return false;
		} else if (datasetWhiteList.size() == 0 && datasetBlackList.size() == 0) {
			Iterator<PrimaryDataset> itPD = config.datasetIterator();
			while (itPD.hasNext())
				requestedDatasets.add(itPD.next().name());
		} else
			applyDatasetFiltering(config);

		if (!filterAllPSets && psetWhiteList.size() > 0) {
			if (psetBlackList.size() > 0) {
				System.err.println(
						"ModifierInstructions.resolve ERROR: " + "white&black lists provided for global psets.");
				return false;
			} else {
				Iterator<PSetParameter> it = config.psetIterator();
				while (it.hasNext()) {
					PSetParameter pset = it.next();
					if (!psetWhiteList.contains(pset.name()))
						psetBlackList.add(pset.name());
				}
			}
		}

		if (!filterAllEDSources && edsourceWhiteList.size() > 0) {
			if (edsourceBlackList.size() > 0) {
				System.err
						.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for edsources.");
				return false;
			} else {
				Iterator<EDSourceInstance> it = config.edsourceIterator();
				while (it.hasNext()) {
					EDSourceInstance edsource = it.next();
					if (!edsourceWhiteList.contains(edsource.name()))
						edsourceBlackList.add(edsource.name());
				}
			}
		}

		if (!filterAllESSources && essourceWhiteList.size() > 0) {
			if (essourceBlackList.size() > 0) {
				System.err
						.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for essources.");
				return false;
			} else {
				Iterator<ESSourceInstance> it = config.essourceIterator();
				while (it.hasNext()) {
					ESSourceInstance essource = it.next();
					if (!essourceWhiteList.contains(essource.name()))
						essourceBlackList.add(essource.name());
				}
			}
		}

		if (!filterAllESModules && esmoduleWhiteList.size() > 0) {
			if (esmoduleBlackList.size() > 0) {
				System.err
						.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for esmodules.");
				return false;
			} else {
				Iterator<ESModuleInstance> it = config.esmoduleIterator();
				while (it.hasNext()) {
					ESModuleInstance esmodule = it.next();
					if (!esmoduleWhiteList.contains(esmodule.name()))
						esmoduleBlackList.add(esmodule.name());
				}
			}
		}

		if (!filterAllServices && serviceWhiteList.size() > 0) {
			if (serviceBlackList.size() > 0) {
				System.err.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for services.");
				return false;
			} else {
				Iterator<ServiceInstance> it = config.serviceIterator();
				while (it.hasNext()) {
					ServiceInstance service = it.next();
					if (!serviceWhiteList.contains(service.name()))
						serviceBlackList.add(service.name());
				}
			}
		}

		if (filterAllOutputModules && pathWhiteList.size() == 0) {
			Iterator<Path> it = config.pathIterator();
			while (it.hasNext()) {
				Path path = it.next();
				if (path.hasOutputModule())
					insertPathIntoBlackList(path.name());
			}
		}

		if (!filterAllPaths && pathWhiteList.size() > 0) {
			if (pathBlackList.size() > 0) {
				System.err.println("ModifierInstructions.resolve ERROR: " + "white&black lists provided for paths.");
				return false;
			} else {
				Iterator<Path> it = config.pathIterator();
				while (it.hasNext()) {
					Path path = it.next();
					if (!pathWhiteList.contains(path.name()))
						pathBlackList.add(path.name());
				}
			}
		}

		// make sure content of requested sequences is requested as well
		ArrayList<Sequence> reqSequences = new ArrayList<Sequence>();
		for (String sequenceName : requestedSequences)
			reqSequences.add(config.sequence(sequenceName));

		Iterator<Sequence> itReqSeq = reqSequences.iterator();
		while (itReqSeq.hasNext()) {
			Sequence sequence = itReqSeq.next();
			Iterator<Reference> itR = sequence.recursiveReferenceIterator();
			while (itR.hasNext()) {
				Reference reference = itR.next();
				Referencable parent = reference.parent();
				String name = parent.name();
				if (isUndefined(parent))
					continue;
				if (parent instanceof Sequence)
					requestSequence(name);
				else if (parent instanceof ModuleInstance)
					requestModule(name);
			}
		}

		// make sure content of undefined sequences is undefined as well
		ArrayList<Sequence> undefSequences = new ArrayList<Sequence>();
		if (undefineAllSequences) {
			Iterator<Sequence> itS = config.sequenceIterator();
			while (itS.hasNext())
				undefSequences.add(itS.next());
		} else {
			for (String sequenceName : undefinedSequences) {
				Sequence sequence = config.sequence(sequenceName);
				if (sequence != null)
					undefSequences.add(sequence);
			}
		}

		Iterator<Sequence> itUndefSeq = undefSequences.iterator();
		while (itUndefSeq.hasNext()) {
			Sequence sequence = itUndefSeq.next();
			Iterator<Sequence> itS = sequence.sequenceIterator();
			while (itS.hasNext()) {
				Sequence s = itS.next();
				if (!isUndefined(s))
					undefineSequence(s.name());
			}
			Iterator<ModuleInstance> itM = sequence.moduleIterator();
			while (itM.hasNext()) {
				ModuleInstance m = itM.next();
				if (!isUndefined(m))
					undefineModule(m.name());
			}
		}

		// no filtering on content
		Iterator<EventContent> itEC = config.contentIterator();
		while (itEC.hasNext())
			requestedContents.add(itEC.next().name());

		return true;
	}

	/** check filter flags */
	public boolean doFilterAll(Class<?> c) {
		if (c == PSetParameter.class)
			return doFilterAllPSets();
		if (c == EDSourceInstance.class)
			return doFilterAllEDSources();
		if (c == ESSourceInstance.class)
			return doFilterAllESSources();
		if (c == ESModuleInstance.class)
			return doFilterAllESModules();
		if (c == ServiceInstance.class)
			return doFilterAllServices();
		if (c == Path.class)
			return doFilterAllPaths();
		if (c == ModuleInstance.class)
			return doFilterAllOutputModules();
		return false;
	}

	public boolean doFilterAllPSets() {
		return filterAllPSets;
	}

	public boolean doFilterAllEDSources() {
		return filterAllEDSources;
	}

	public boolean doFilterAllESSources() {
		return filterAllESSources;
	}

	public boolean doFilterAllESModules() {
		return filterAllESModules;
	}

	public boolean doFilterAllServices() {
		return filterAllServices;
	}

	public boolean doFilterAllPaths() {
		return filterAllPaths;
	}

	public boolean doFilterAllOutputModules() {
		return filterAllOutputModules;
	}

	/** check if passed object is in a blacklist */
	public boolean isInBlackList(Object obj) {
		if (obj instanceof PSetParameter) {
			PSetParameter pset = (PSetParameter) obj;
			return (filterAllPSets || psetBlackList.contains(pset.name()));
		} else if (obj instanceof EDSourceInstance) {
			EDSourceInstance edsource = (EDSourceInstance) obj;
			return (filterAllEDSources || edsourceBlackList.contains(edsource.name()));
		} else if (obj instanceof ESSourceInstance) {
			ESSourceInstance essource = (ESSourceInstance) obj;
			return (filterAllESSources || essourceBlackList.contains(essource.name()));
		} else if (obj instanceof ESModuleInstance) {
			ESModuleInstance esmodule = (ESModuleInstance) obj;
			return (filterAllESModules || esmoduleBlackList.contains(esmodule.name()));
		} else if (obj instanceof ServiceInstance) {
			ServiceInstance service = (ServiceInstance) obj;
			return (filterAllServices || serviceBlackList.contains(service.name()));
		} else if (obj instanceof Path) {
			Path path = (Path) obj;
			return (filterAllPaths || pathBlackList.contains(path.name()));
		}
		return false;
	}

	/** check if an EDSource is to be added */
	public boolean doInsertEDSource() {
		return (edsourceT != null);
	}

	/** retrieve the EDSource to be added */
	public EDSourceInstance edsourceToBeAdded() {
		if (edsourceT == null)
			return null;
		edsourceT.removeAllInstances();
		EDSourceInstance result = null;
		try {
			result = (EDSourceInstance) edsourceT.instance();
		} catch (DataException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}

	/**
	 * retrieve the value of the fileNames parameter for [Pool]Source to be added
	 */
	public String edsourceFileNames() {
		return edsourceFileNames;
	}

	/** check if an OutputModule is to be added */
	public boolean doInsertOutputModule() {
		return (outputT != null);
	}

	/** *retrieve the OutputModule template to be added */
	public ModuleInstance outputModuleToBeAdded(String name) {
		if (outputT == null)
			return null;
		ModuleInstance result = null;
		try {
			result = (ModuleInstance) outputT.instance(name);
		} catch (DataException e) {
			System.err.println(e.getMessage());
		}
		return result;
	}

	/** check if a sequence or module is specifically requested */
	public boolean isRequested(Referencable moduleOrSequence) {
		if (moduleOrSequence instanceof Sequence)
			return (requestedSequences.contains(moduleOrSequence.name()));
		else if (moduleOrSequence instanceof ModuleInstance)
			return (requestedModules.contains(moduleOrSequence.name()));
		else if (moduleOrSequence instanceof OutputModule)
			return (requestedOutputs.contains(moduleOrSequence.name()));
		return false;
	}

	/** check if a sequence or module should be undefined */
	public boolean isUndefined(Referencable moduleOrSequence) {
		if (moduleOrSequence instanceof Sequence)
			return (undefineAllSequences) ? true : (undefinedSequences.contains(moduleOrSequence.name()));
		else if ((moduleOrSequence instanceof ModuleInstance) || (moduleOrSequence instanceof OutputModule))
			return (undefineAllModules) ? true : (undefinedModules.contains(moduleOrSequence.name()));
		return false;
	}

	/** get iterator for requested sequences */
	public Iterator<String> requestedSequenceIterator() {
		return requestedSequences.iterator();
	}

	/** get iterator for requested modules */
	public Iterator<String> requestedModuleIterator() {
		return requestedModules.iterator();
	}

	/** get iterator for requested outputs */
	public Iterator<String> requestedOutputIterator() {
		return requestedOutputs.iterator();
	}

	/** get iterator for requested contents */
	public Iterator<String> requestedContentIterator() {
		return requestedContents.iterator();
	}

	/** get iterator for requested streams */
	public Iterator<String> requestedStreamIterator() {
		return requestedStreams.iterator();
	}

	/** get iterator for requested datasets */
	public Iterator<String> requestedDatasetIterator() {
		return requestedDatasets.iterator();
	}

	/** get iterator over requested blocks */
	public Iterator<String> blockIterator() {
		return blocks.iterator();
	}

	/** filter all plugins of a certain type */
	public void filterAll(Class<?> c, boolean filter) {
		if (c == PSetParameter.class)
			filterAllPSets(filter);
		else if (c == EDSourceInstance.class)
			filterAllEDSources(filter);
		else if (c == ESSourceInstance.class)
			filterAllESSources(filter);
		else if (c == ESModuleInstance.class)
			filterAllESModules(filter);
		else if (c == ServiceInstance.class)
			filterAllServices(filter);
		else if (c == Path.class)
			filterAllPaths(filter);
		else
			System.err.println("ERROR: can't filterAll of type " + c.getName());
	}

	public void filterAllPSets(boolean filter) {
		filterAllPSets = filter;
		if (!filter)
			psetBlackList.clear();
	}

	public void filterAllEDSources(boolean filter) {
		filterAllEDSources = filter;
		if (!filter)
			edsourceBlackList.clear();
	}

	public void filterAllESSources(boolean filter) {
		filterAllESSources = filter;
		if (!filter)
			essourceBlackList.clear();
	}

	public void filterAllESModules(boolean filter) {
		filterAllESModules = filter;
		if (!filter)
			esmoduleBlackList.clear();
	}

	public void filterAllServices(boolean filter) {
		filterAllServices = filter;
		if (!filter)
			serviceBlackList.clear();
	}

	public void filterAllPaths(boolean filter) {
		filterAllPaths = filter;
		if (!filter)
			pathBlackList.clear();
	}

	public void filterAllOutputModules(boolean filter) {
		filterAllOutputModules = filter;
	}

	public void filterAllPSets(boolean filter, IConfiguration config) {
		filterAllPSets = filter;
		psetBlackList.clear();
		if (filter) {
			Iterator<PSetParameter> itPSet = config.psetIterator();
			while (itPSet.hasNext())
				psetBlackList.add(itPSet.next().name());
		}
	}

	public void filterAllEDSources(boolean filter, IConfiguration config) {
		filterAllEDSources = filter;
		edsourceBlackList.clear();
		if (filter) {
			Iterator<EDSourceInstance> itEDS = config.edsourceIterator();
			while (itEDS.hasNext())
				edsourceBlackList.add(itEDS.next().name());
		}
	}

	public void filterAllESSources(boolean filter, IConfiguration config) {
		filterAllESSources = filter;
		essourceBlackList.clear();
		if (filter) {
			Iterator<ESSourceInstance> itESS = config.essourceIterator();
			while (itESS.hasNext())
				essourceBlackList.add(itESS.next().name());
		}
	}

	public void filterAllESModules(boolean filter, IConfiguration config) {
		filterAllESModules = filter;
		esmoduleBlackList.clear();
		if (filter) {
			Iterator<ESModuleInstance> itESM = config.esmoduleIterator();
			while (itESM.hasNext())
				esmoduleBlackList.add(itESM.next().name());
		}
	}

	public void filterAllServices(boolean filter, IConfiguration config) {
		filterAllServices = filter;
		serviceBlackList.clear();
		if (filter) {
			Iterator<ServiceInstance> itSvc = config.serviceIterator();
			while (itSvc.hasNext())
				serviceBlackList.add(itSvc.next().name());
		}
	}

	public void filterAllPaths(boolean filter, IConfiguration config) {
		filterAllPaths = filter;
		pathBlackList.clear();
		if (filter) {
			Iterator<Path> itP = config.pathIterator();
			while (itP.hasNext())
				pathBlackList.add(itP.next().name());
		}
	}

	/** insert/remove components into the corresponding whitelist/blacklist */
	public int insertIntoBlackList(Object o) {
		String name = null;
		if (o instanceof Referencable)
			name = ((Referencable) o).name();
		else if (o instanceof Instance)
			name = ((Instance) o).name();
		else if (o instanceof OutputModule)
			name = ((OutputModule) o).name();
		else if (o instanceof PSetParameter)
			name = ((Parameter) o).name();

		ArrayList<String> blacklist = null;
		if (o instanceof PSetParameter)
			blacklist = psetBlackList;
		else if (o instanceof EDSourceInstance)
			blacklist = edsourceBlackList;
		else if (o instanceof ESSourceInstance)
			blacklist = essourceBlackList;
		else if (o instanceof ESModuleInstance)
			blacklist = esmoduleBlackList;
		else if (o instanceof ServiceInstance)
			blacklist = serviceBlackList;
		else if (o instanceof Path)
			blacklist = pathBlackList;

		if (name == null)
			System.err.println("ERROR: name is null");
		if (blacklist == null)
			System.err.println("ERROR: blacklist is null");

		blacklist.add(name);
		return blacklist.size();
	}

	public int removeFromBlackList(Object o) {
		String name = null;
		if (o instanceof Referencable)
			name = ((Referencable) o).name();
		else if (o instanceof Instance)
			name = ((Instance) o).name();
		else if (o instanceof OutputModule)
			name = ((OutputModule) o).name();
		else if (o instanceof PSetParameter)
			name = ((Parameter) o).name();

		if (o instanceof PSetParameter) {
			int index = psetBlackList.indexOf(name);
			if (index >= 0) {
				filterAllPSets = false;
				psetBlackList.remove(name);
				return psetBlackList.size();
			}
		} else if (o instanceof EDSourceInstance) {
			int index = edsourceBlackList.indexOf(name);
			if (index >= 0) {
				filterAllEDSources = false;
				edsourceBlackList.remove(name);
				return edsourceBlackList.size();
			}
		} else if (o instanceof ESSourceInstance) {
			int index = essourceBlackList.indexOf(name);
			if (index >= 0) {
				filterAllESSources = false;
				essourceBlackList.remove(name);
				return essourceBlackList.size();
			}
		} else if (o instanceof ESModuleInstance) {
			int index = esmoduleBlackList.indexOf(name);
			if (index >= 0) {
				filterAllESModules = false;
				esmoduleBlackList.remove(name);
				return esmoduleBlackList.size();
			}
		} else if (o instanceof ServiceInstance) {
			int index = serviceBlackList.indexOf(name);
			if (index >= 0) {
				filterAllServices = false;
				serviceBlackList.remove(name);
				return serviceBlackList.size();
			}
		} else if (o instanceof Path) {
			int index = pathBlackList.indexOf(name);
			if (index >= 0) {
				filterAllPaths = false;
				pathBlackList.remove(name);
				return pathBlackList.size();
			}
		}

		return -1;
	}

	public void insertPSetIntoBlackList(String psetName) {
		psetBlackList.add(psetName);
	}

	public void insertPSetIntoWhiteList(String psetName) {
		psetWhiteList.add(psetName);
	}

	public void insertEDSourceIntoBlackList(String edsourceName) {
		edsourceBlackList.add(edsourceName);
	}

	public void insertEDSourceIntoWhiteList(String edsourceName) {
		edsourceWhiteList.add(edsourceName);
	}

	public void insertESSourceIntoBlackList(String essourceName) {
		essourceBlackList.add(essourceName);
	}

	public void insertESSourceIntoWhiteList(String essourceName) {
		essourceWhiteList.add(essourceName);
	}

	public void insertESModuleIntoBlackList(String esmoduleName) {
		esmoduleBlackList.add(esmoduleName);
	}

	public void insertESModuleIntoWhiteList(String esmoduleName) {
		esmoduleWhiteList.add(esmoduleName);
	}

	public void insertServiceIntoBlackList(String serviceName) {
		serviceBlackList.add(serviceName);
	}

	public void insertServiceIntoWhiteList(String serviceName) {
		serviceWhiteList.add(serviceName);
	}

	public void insertPathIntoBlackList(String pathName) {
		pathBlackList.add(pathName);
	}

	public void insertPathIntoWhiteList(String pathName) {
		pathWhiteList.add(pathName);
	}

	public void insertDatasetIntoBlackList(String dataset) {
		datasetBlackList.add(dataset);
	}

	public void insertDatasetIntoWhiteList(String dataset) {
		datasetWhiteList.add(dataset);
	}

	public void insertStreamIntoBlackList(String stream) {
		streamBlackList.add(stream);
	}

	public void insertStreamIntoWhiteList(String stream) {
		streamWhiteList.add(stream);
	}

	/** request a sequence regardless of it being referenced in path */
	public void requestSequence(String sequenceName) {
		requestedSequences.add(sequenceName);
	}

	/** unrequest a sequence regardless of it being referenced in path */
	public void unrequestSequence(String sequenceName) {
		requestedSequences.remove(sequenceName);
	}

	/** no sequences will be defined */
	public void undefineAllSequences() {
		undefineAllSequences = true;
	}

	/** sequence won't be defined but references remain; content removed! */
	public void undefineSequence(String sequenceName) {
		undefinedSequences.add(sequenceName);
	}

	/** remove sequence from list of undefined sequences */
	public void redefineSequence(String sequenceName) {
		undefinedSequences.remove(sequenceName);
	}

	/** request a module regardless of it being referenced in path */
	public void requestModule(String moduleName) {
		requestedModules.add(moduleName);
	}

	/** unrequest a module regardless of it being referenced in path */
	public void unrequestModule(String moduleName) {
		requestedModules.remove(moduleName);
	}

	/** request a output regardless of it being referenced in path */
	public void requestOutput(String outputName) {
		requestedOutputs.add(outputName);
	}

	/** unrequest a output regardless of it being referenced in path */
	public void unrequestOutput(String outputName) {
		requestedOutputs.remove(outputName);
	}

	/** request a content regardless of it being referenced in path */
	public void requestContent(String contentName) {
		requestedContents.add(contentName);
	}

	/** request a stream regardless of it being referenced in path */
	public void requestStream(String streamName) {
		requestedStreams.add(streamName);
	}

	/** request a dataset regardless of it being referenced in path */
	public void requestDataset(String datasetName) {
		requestedDatasets.add(datasetName);
	}

	/** no modules will be defined */
	public void undefineAllModules() {
		undefineAllModules = true;
	}

	/** module will not be defined, but references remain */
	public void undefineModule(String moduleName) {
		undefinedModules.add(moduleName);
	}

	/** remove a module from the list of undefined modules */
	public void redefineModule(String moduleName) {
		undefinedModules.remove(moduleName);
	}

	/** insert a DaqSource */
	public void insertDaqSource() {
		filterAllEDSources = true;
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new StringParameter("readerPluginName", "FUShmReader", false));
		edsourceT = new EDSourceTemplate("DaqSource", "UNKNOWN", params);
	}

	/** insert PoolSource [fileNames = comma-separated list!] */
	public void insertPoolSource(String fileNames) {
		filterAllEDSources = true;
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new VStringParameter("fileNames", "", false));
		edsourceT = new EDSourceTemplate("PoolSource", "UNKNOWN", params);
		edsourceFileNames = fileNames;
	}

	/** insert/replace FUShmStreamConsumer */
	public void insertShmStreamConsumer() {
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new PSetParameter("SelectEvents", "", false));
		params.add(new VStringParameter("outputCommands", "", false));
		params.add(new BoolParameter("use_compression", "true", false));
		params.add(new Int32Parameter("compression_level", "1", false));
		params.add(new Int32Parameter("max_event_size", "7000000", false));
		outputT = new ModuleTemplate("ShmStreamConsumer", "UNKNOWN", params, "OutputModule");
	}

	/** insert/replace PoolOutputModule */
	public void insertPoolOutputModule(String fileName) {
		if (fileName.split(":").length == 1)
			fileName = "file:" + fileName;
		ArrayList<Parameter> params = new ArrayList<Parameter>();
		params.add(new StringParameter("fileName", fileName, false));
		params.add(new PSetParameter("SelectEvents", "", false));
		params.add(new VStringParameter("outputCommands", "", false));
		outputT = new ModuleTemplate("PoolOutputModule", "UNKNOWN", params, "OutputModule");
	}

	/**
	 * method to filter config according to streams specified in white/black list
	 * streams in streamWhitelist are added to requestedStreams, all datasets of
	 * these streams are added to datasetWhiteList streams in streamBlacklist are
	 * NOT added to requestedStreams, all datasets of these streams are added to
	 * datasetBlackList
	 * 
	 * @param config original config to be modified
	 */
	protected void applyStreamFiltering(IConfiguration config) {
		if (streamWhiteList.size() > 0) {
			Iterator<Stream> streamIT = config.streamIterator();
			while (streamIT.hasNext()) {
				Stream stream = streamIT.next();
				if (streamWhiteList.contains(stream.name())) {
					requestedStreams.add(stream.name());
					Iterator<PrimaryDataset> datasetIT = stream.datasetIterator();
					while (datasetIT.hasNext())
						datasetWhiteList.add(datasetIT.next().name());
				}
			}
		} else if (streamBlackList.size() > 0) {
			Iterator<Stream> streamIT = config.streamIterator();
			while (streamIT.hasNext()) {
				Stream stream = streamIT.next();
				if (streamBlackList.contains(stream.name())) {
					Iterator<PrimaryDataset> datasetIT = stream.datasetIterator();
					while (datasetIT.hasNext())
						datasetBlackList.add(datasetIT.next().name());
				} else
					requestedStreams.add(stream.name());
			}
		}
	}

	/**
	 * method to filter config according to datasets specified in white/black list
	 * datasets in datasetWhitelist are added to requestedDatasets, all paths of
	 * these datasets are added to pathWhiteList datasets in datasetBlacklist are
	 * NOT added to requestedDatasets, all paths of these datasets are added to
	 * pathBlackList
	 * 
	 * @param config original config to be modified
	 */
	protected void applyDatasetFiltering(IConfiguration config) {
		if (datasetWhiteList.size() > 0) {
			Iterator<PrimaryDataset> itPD = config.datasetIterator();
			while (itPD.hasNext()) {
				PrimaryDataset dataset = itPD.next();
				if (datasetWhiteList.contains(dataset.name())) {
					requestedDatasets.add(dataset.name());
					Iterator<Path> pathList = dataset.orderedPathIterator();
					while (pathList.hasNext())
						pathWhiteList.add(pathList.next().name());
				}
			}
		} else if (datasetBlackList.size() > 0) {
			Iterator<PrimaryDataset> itPD = config.datasetIterator();
			while (itPD.hasNext()) {
				PrimaryDataset dataset = itPD.next();
				if (datasetBlackList.contains(dataset.name())) {
					Iterator<Path> pathList = dataset.orderedPathIterator();
					while (pathList.hasNext())
						pathBlackList.add(pathList.next().name());
				} else
					requestedDatasets.add(dataset.name());
			}
		}
	}

}
