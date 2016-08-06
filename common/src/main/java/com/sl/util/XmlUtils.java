package com.sl.util;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xml util
 */
public class XmlUtils {
	
	/**
	 * 解析字符串xml
	 * @param xmlStr
	 * @return
	 */
	public static Document getDocumentTxt(String xmlStr) {
		Document doc = null;
		try {
			doc = DocumentHelper.parseText(xmlStr);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return doc;
	}
	
	/**
	 * 获取远程xml
	 * @param remoteUrl http path
	 * @return
	 */
	public static Document getDocumentUrl(String remoteUrl) {
		Document doc = null;
		try {
			doc = getDocument(new URL(remoteUrl));
		} catch (MalformedURLException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return doc;
	}

	/**
	 * 获取本地file xml
	 * @param xmlFilePath localPath
	 * @return
	 */
	public static Document getDocument(String xmlFilePath) {
		
		if (StringUtils.isBlank(xmlFilePath)) {
			throw new RuntimeException("parameter is not null!");
		}
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(new File(xmlFilePath));
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return document;
	}

	/**
	 * 获取远程http xml
	 * @param url url
	 * @return
	 */
	public static Document getDocument(URL url) {
		if (null == url) {
			throw new RuntimeException("parameter is not null");
		}
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(url);
		} catch (DocumentException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
		return document;
	}

	/**
	 * get root element
	 * @param document
	 * @return
	 */
	public static Element getRootElement(Document document) {
		if (null != document) {
			return document.getRootElement();
		}
		return null;
	}

	/**
	 * add element
	 * @param elementName
	 * @return
	 */
	public static Element addElement(String elementName) {
		if (elementName == null || elementName.equals("")) {
			throw new RuntimeException("parameter is not null!");
		}
		return new DOMElement(elementName);
	}

	/**
	 * 
	 * @param element
	 * @param elementName
	 * @param attributeName
	 * @param attributeValue
	 */
	public static void addElement(Element element, String elementName,
			String attributeName, String attributeValue) {
		
		if (null == element) {
			throw new RuntimeException("parameter element is not null!");
		}

		if (StringUtils.isBlank(attributeName)) return;
		
		String[] arrAttributeName = attributeName.split("[,;]");
		String[] arrAttributeValue = attributeValue.split("[,;]");

		if (arrAttributeName.length != arrAttributeValue.length) {
			throw new RuntimeException("parameter is not correct!");
		}
		Element tempElement = new DOMElement(elementName);
		for (int i = 0; i < arrAttributeName.length; i++) {
			tempElement.addAttribute(arrAttributeName[i], arrAttributeValue[i]);

		}
		element.add(tempElement);
	}

	/**
	 * 
	 * @param document
	 * @param xmlFilePath
	 */
	public static void saveXML(Document document, String xmlFilePath,
			String encoding) {
		XMLWriter out = null;
		try {
			OutputFormat outputFormat = OutputFormat.createPrettyPrint();
			outputFormat.setEncoding(encoding);
			out = new XMLWriter(new FileWriter(xmlFilePath), outputFormat);
			out.write(document);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new RuntimeException(e.getMessage(), e);
		} finally {
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			}
		}
	}
	
	/**
	 * 获取xpath值
	 * @param em
	 * @param xpath
	 * @return
	 */
	public static String getSelectSingleNodeValue(Element em, String xpath) {
		Object obj = em.selectSingleNode(xpath);
		if (null != obj) {
			Element ele = (Element) obj;
			return StringUtils.trim(ele.getStringValue());
		} else {
			return "";
		}
	}
	
	/**
	 * 获取节点属性值
	 * @param em
	 * @param attrName
	 * @param xpath
	 * @return
	 */
	public static String getSelectSingleNodeAttrValue(Element em, String attrName, String xpath) {
		Object obj = em.selectSingleNode(xpath);
		if (null != obj) {
			Element ele = (Element) obj;
			return StringUtils.trim(ele.attributeValue(attrName, ""));
		} else {
			return "";
		}
	}
	
	/**
	 * 获取节点属性值(默认属性名 value)
	 * @param em
	 * @param xpath
	 * @return
	 */
	public static String getSelectSingleNodeAttrValue(Element em, String xpath) {
		return getSelectSingleNodeAttrValue(em, "value", xpath);
	}
	
	/**
	 * 
	 * @param msg
	 * @return
	 * @throws Exception
	 */
	public static Map<String, String> parseXml(String msg) {
		Map<String, String> map = new HashMap<String, String>();
		Document doc = XmlUtils.getDocumentTxt(msg);
		Element rootEl = doc.getRootElement();
		List<Element> list = rootEl.elements();
		for (int i = 0; i < list.size(); i++) {
			Element element = list.get(i);
			map.put(element.getName(), element.getText());
		}
		return map;
	}

}
