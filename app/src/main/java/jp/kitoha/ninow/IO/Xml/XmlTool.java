// ************************************************************************************************
//　 [システム]　EviQA
//　 [ソースファイル名]　XmlTool.java
//　 [機能説明]　XML操作ツール
// ------------------------------------------------------------------------------------------------
//　 バージョン           変更日　　        担当者　　　      変更内容
// ------------------------------------------------------------------------------------------------
//　 1.00.00.00 2016- 1-14　t.kawaguchi    新規作成				IESのまま
// ************************************************************************************************

package jp.kitoha.ninow.IO.Xml;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class XmlTool {
	/**
	 * @brief
	 * @param	qualifiedName
	 * @return
	 * @throws ParserConfigurationException
	 */
	public static Document generateDocumentSkelton(String qualifiedName) throws ParserConfigurationException {
		DocumentBuilderFactory	factory		= DocumentBuilderFactory.newInstance();				//XML読込用インスタンスの取得
		DocumentBuilder			builder		= factory.newDocumentBuilder();						//
		DOMImplementation		domImpl		= builder.getDOMImplementation();					//Documentノードの生成
		Document				document	= domImpl.createDocument("",qualifiedName,null);	//Documentに代入する
		return document;
	}

	/**
	 * @brief	文字情報の取得
	 * @param	document	XMLドキュメント
	 * @return	文字情報
	 * @throws	TransformerException
	 * @throws	IOException
	 */
	public static String getString(Document document) throws TransformerException, IOException {
		ByteArrayOutputStream	b	= new ByteArrayOutputStream();								//バイト配列
		OutputStream			os	= new BufferedOutputStream( b );							//出力ストリーム
		outputDocument( os, document );
		return b.toString( "UTF-8" );
	}

	/**
	 * ファイルからドキュメント形式に変換する
	 * @param file(in)		出力ファイル情報
	 * @param document(in)	ドキュメント(出力内容)
	 * @throws TransformerException
	 * @throws IOException
	 */
	public static void writeDocument(File file, Document document) throws TransformerException, IOException {
		FileOutputStream os = new FileOutputStream( file );
		outputDocument(os, document);
	}

	/**
	 * ファイルからドキュメント形式に変換する
	 * @param file(in)		出力ファイル情報
	 * @param document(in)	ドキュメント(出力内容)
	 * @throws TransformerException
	 * @throws IOException
	 */
	public static void writeDocument(OutputStream file, Document document) throws TransformerException, IOException {
		outputDocument( file, document );
	}

	/**
	 * ドキュメント出力
	 * @param os(out)		出力ストリーム
	 * @param document(in)	ドキュメント(出力内容)
	 * @throws TransformerException
	 * @throws IOException
	 */
	private static void outputDocument(OutputStream os, Document document) throws TransformerException, IOException {
		// org.apache.xml.serializer.OutputPropertiesFactory.S_KEY_INDENT_AMOUNT
		final String S_KEY_INDENT_AMOUNT = "{http://xml.apache.org/xalan}indent-amount";

		TransformerFactory	transFactory = TransformerFactory.newInstance();
		Transformer transformer = transFactory.newTransformer();
		transformer.setOutputProperty( OutputKeys.INDENT,	"yes" );
		transformer.setOutputProperty( OutputKeys.METHOD,	"xml" );
		transformer.setOutputProperty( S_KEY_INDENT_AMOUNT,	"2" );

		DOMSource		source = new DOMSource( document );
		StreamResult	result = new StreamResult( os );
		transformer.transform( source, result );
		os.flush();
		os.close();
	}

	/**
	 * ノードの追加
	 * @param document(in)	ドキュメント
	 * @param node(in)		要素情報
	 * @param name(in)		項目名
	 * @param value(in)		値
	 */
	public static void appendNode(Document document, Element node, String name, String value){
		if( value == null ){
			value = "";
		}
		Element	child = document.createElement( name );
		child.appendChild(document.createTextNode(value));
		node.appendChild(child);
	}


	/****************************************************************
	 * 値の取得
	 ****************************************************************/
	/**
	 * @brief	値の取得
	 * @param	root(in)	要素情報
	 * @param	name(in)	項目名
	 * @return
	 */
	public static String getValue(Element root, String name){
		NodeList listRoot = root.getElementsByTagName( name );
		if( listRoot.getLength() == 0 ){
			return "";
		}

		Node node	= listRoot.item( 0 );
		Node child	= node.getFirstChild();

		if( child != null ){
			return child.getNodeValue();
		} else {
			return "";
		}
	}

	/**
	 * @brief	値リストの取得
	 * @param	root(in)	要素情報
	 * @param	name(in)	項目名
	 * @return	指定した項目の値リスト
	 */
	public static List<String> getValueList(Element root, String name){
		List<String> result = new ArrayList<String>();
		NodeList listRoot = root.getElementsByTagName(name);
		if( listRoot.getLength() == 0 ){
			return result;
		}
		for(int i=0; i < listRoot.getLength(); i++){
			Node node = listRoot.item(i);
			Node child = node.getFirstChild();

			if (child != null){
				result.add(child.getNodeValue());
			}
		}

		return result;
	}

	/**
	 * @brief	値の取得
	 * @param	nodeList(in)	ノードリスト
	 * @param	name(in)		項目名
	 * @return	指定した項目の値
	 */
	public static String getValue(NodeList nodeList, String name){
		for(int i=0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeName().equals(name)){
				Node child = node.getFirstChild();
				if (child != null){
					return child.getNodeValue();
				} else {
					return "";
				}
			}
		}
		return "";
	}

	/**
	 * @brief	値の取得
	 * @param	map(in)		ノードマップ
	 * @param	name(in)	項目名
	 * @return	指定した項目の値
	 */
	public static String getValue(NamedNodeMap map, String name) {
		Node node = map.getNamedItem(name);
		if (node == null){
			return "";
		}
		return node.getNodeValue();
	}
}
