package org.openidams.languages;

public class PluggableLanguageDescriptor {

	private String languageName;
	private String compilerClass;
	private String interpreterClass;
	private String scriptFileExtension;
	/**
	 * @param name
	 * @param extension
	 * @param class1
	 * @param class2
	 */
	public PluggableLanguageDescriptor(String name, String extension, String compClass, String interpClass) {
		languageName = name;
		scriptFileExtension = extension;
		compilerClass = compClass;
		interpreterClass = interpClass;
	}
	/**
	 * @return Returns the compilerClass.
	 */
	public String getCompilerClass() {
		return compilerClass;
	}
	/**
	 * @return Returns the interpreterClass.
	 */
	public String getInterpreterClass() {
		return interpreterClass;
	}
	/**
	 * @return Returns the languageName.
	 */
	public String getLanguageName() {
		return languageName;
	}
	/**
	 * @return Returns the scriptFileExtension.
	 */
	public String getScriptFileExtension() {
		return scriptFileExtension;
	}
}
