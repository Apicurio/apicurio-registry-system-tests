package io.apicurio.registry.systemtests.client;

public final class ArtifactContent {
    public static final String DEFAULT_AVRO = "{\"name\":\"price\",\"namespace\":\"com.example\",\"type\":\"record\"," +
            "\"fields\":[{\"name\":\"symbol\",\"type\":\"string\"},{\"name\":\"price\",\"type\":\"string\"}]}";
    public static final String DEFAULT_AVRO_UPDATED = "{\"key\":\"id\"}";

    public static final String DEFAULT_PROTOBUF = "syntax = \"proto3\";\n" +
            "\n" +
            "message SearchRequest {\n" +
            "  string query = 1;\n" +
            "  int32 page_number = 2;\n" +
            "  int32 results_per_page = 3;\n" +
            "}";
    public static final String DEFAULT_PROTOBUF_UPDATED = "syntax = \"proto3\";\n" +
            "\n" +
            "message SearchRequestUpdated {\n" +
            "  string query = 10;\n" +
            "  int32 page_number = 20;\n" +
            "  int32 results_per_page = 30;\n" +
            "}";

    public static final String DEFAULT_JSON = DEFAULT_AVRO;
    public static final String DEFAULT_JSON_UPDATED = DEFAULT_AVRO_UPDATED;

    public static final String DEFAULT_OPENAPI = "openapi: 3.0.0\n" +
            "info:\n" +
            "  title: Sample API\n" +
            "  description: Optional multiline or single-line description in " +
            "[CommonMark](http://commonmark.org/help/) or HTML.\n" +
            "  version: 0.1.9\n" +
            "\n" +
            "servers:\n" +
            "  - url: http://api.example.com/v1\n" +
            "    description: Optional server description, e.g. Main (production) server\n" +
            "  - url: http://staging-api.example.com\n" +
            "    description: Optional server description, e.g. Internal staging server for testing\n" +
            "\n" +
            "paths:\n" +
            "  /users:\n" +
            "    get:\n" +
            "      summary: Returns a list of users.\n" +
            "      description: Optional extended description in CommonMark or HTML.\n" +
            "      responses:\n" +
            "        \"200\": # status code\n" +
            "          description: A JSON array of user names\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  type: string";
    public static final String DEFAULT_OPENAPI_UPDATED = "openapi: 3.0.0\n" +
            "info:\n" +
            "  title: Sample API Updated\n" +
            "  description: Optional multiline or single-line description in " +
            "[CommonMark](http://commonmark.org/help/) or HTML.\n" +
            "  version: 0.2.0\n" +
            "\n" +
            "servers:\n" +
            "  - url: http://api.example.com/v2\n" +
            "    description: Optional server description, e.g. Main (production) server\n" +
            "  - url: http://staging-api.example.com\n" +
            "    description: Optional server description, e.g. Internal staging server for testing\n" +
            "\n" +
            "paths:\n" +
            "  /users:\n" +
            "    get:\n" +
            "      summary: Returns a list of users.\n" +
            "      description: Optional extended description in CommonMark or HTML.\n" +
            "      responses:\n" +
            "        \"200\": # status code\n" +
            "          description: A JSON array of user names\n" +
            "          content:\n" +
            "            application/json:\n" +
            "              schema:\n" +
            "                type: array\n" +
            "                items:\n" +
            "                  type: string";

    public static final String DEFAULT_ASYNCAPI = "asyncapi: 3.0.0\n" +
            "info:\n" +
            "  title: Hello world application\n" +
            "  version: '0.1.0'\n" +
            "channels:\n" +
            "  hello:\n" +
            "    address: 'hello'\n" +
            "    messages:\n" +
            "      sayHelloMessage:\n" +
            "        payload:\n" +
            "          type: string\n" +
            "          pattern: '^hello .+$'\n" +
            "operations:\n" +
            "  receiveHello:\n" +
            "    action: 'receive'\n" +
            "    channel:\n" +
            "      $ref: '#/channels/hello'";
    public static final String DEFAULT_ASYNCAPI_UPDATED = "asyncapi: 3.0.0\n" +
            "info:\n" +
            "  title: Hello world application v2\n" +
            "  version: '0.2.0'\n" +
            "channels:\n" +
            "  hello:\n" +
            "    address: 'hello2'\n" +
            "    messages:\n" +
            "      sayHelloMessage:\n" +
            "        payload:\n" +
            "          type: string\n" +
            "          pattern: '^hello2 .+$'\n" +
            "operations:\n" +
            "  receiveHello:\n" +
            "    action: 'receive'\n" +
            "    channel:\n" +
            "      $ref: '#/channels/hello2'";

    public static final String DEFAULT_GRAPHQL = "type Query {\n" +
            "    bookById(id: ID): Book\n" +
            "}\n" +
            "\n" +
            "type Book {\n" +
            "    id: ID\n" +
            "    name: String\n" +
            "    pageCount: Int\n" +
            "    author: Author\n" +
            "}\n" +
            "\n" +
            "type Author {\n" +
            "    id: ID\n" +
            "    firstName: String\n" +
            "    lastName: String\n" +
            "}";
    public static final String DEFAULT_GRAPHQL_UPDATED = "type Query {\n" +
            "    bookById(id: ID): Book\n" +
            "}\n" +
            "\n" +
            "type Book {\n" +
            "    id: ID\n" +
            "    name: String\n" +
            "    pageCount: Int\n" +
            "    author: BookAuthor\n" +
            "}\n" +
            "\n" +
            "type BookAuthor {\n" +
            "    id: ID\n" +
            "    firstName: String\n" +
            "    lastName: String\n" +
            "}";

    public static final String DEFAULT_KCONNECT = "{\n" +
            "    \"type\": \"struct\",\n" +
            "    \"fields\": [\n" +
            "        {\n" +
            "            \"type\": \"string\",\n" +
            "            \"optional\": false,\n" +
            "            \"field\": \"bar\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"optional\": false\n" +
            "}";
    public static final String DEFAULT_KCONNECT_UPDATED = "{\n" +
            "    \"type\": \"struct\",\n" +
            "    \"fields\": [\n" +
            "        {\n" +
            "            \"type\": \"string\",\n" +
            "            \"optional\": false,\n" +
            "            \"field\": \"bar\"\n" +
            "        },\n" +
            "        {\n" +
            "            \"type\": \"string\",\n" +
            "            \"optional\": true,\n" +
            "            \"field\": \"foo\"\n" +
            "        }\n" +
            "    ],\n" +
            "    \"optional\": false\n" +
            "}";

    public static final String DEFAULT_WSDL = "<message name=\"getTermRequest\">\n" +
            "  <part name=\"term\" type=\"xs:string\"/>\n" +
            "</message>\n" +
            "\n" +
            "<message name=\"getTermResponse\">\n" +
            "  <part name=\"value\" type=\"xs:string\"/>\n" +
            "</message>\n" +
            "\n" +
            "<portType name=\"glossaryTerms\">\n" +
            "  <operation name=\"getTerm\">\n" +
            "    <input message=\"getTermRequest\"/>\n" +
            "    <output message=\"getTermResponse\"/>\n" +
            "  </operation>\n" +
            "</portType> ";
    public static final String DEFAULT_WSDL_UPDATED = "<message name=\"getTermRequest2\">\n" +
            "  <part name=\"term\" type=\"xs:string\"/>\n" +
            "</message>\n" +
            "\n" +
            "<message name=\"getTermResponse2\">\n" +
            "  <part name=\"value\" type=\"xs:string\"/>\n" +
            "</message>\n" +
            "\n" +
            "<portType name=\"glossaryTerms2\">\n" +
            "  <operation name=\"getTerm2\">\n" +
            "    <input message=\"getTermRequest2\"/>\n" +
            "    <output message=\"getTermResponse2\"/>\n" +
            "  </operation>\n" +
            "</portType> ";

    public static final String DEFAULT_XSD = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "           xmlns:tns=\"http://tempuri.org/PurchaseOrderSchema.xsd\"\n" +
            "           targetNamespace=\"http://tempuri.org/PurchaseOrderSchema.xsd\"\n" +
            "           elementFormDefault=\"qualified\">\n" +
            " <xsd:element name=\"PurchaseOrder\" type=\"tns:PurchaseOrderType\"/>\n" +
            " <xsd:complexType name=\"PurchaseOrderType\">\n" +
            "  <xsd:sequence>\n" +
            "   <xsd:element name=\"ShipTo\" type=\"tns:USAddress\" maxOccurs=\"2\"/>\n" +
            "   <xsd:element name=\"BillTo\" type=\"tns:USAddress\"/>\n" +
            "  </xsd:sequence>\n" +
            "  <xsd:attribute name=\"OrderDate\" type=\"xsd:date\"/>\n" +
            " </xsd:complexType>\n" +
            "\n" +
            " <xsd:complexType name=\"USAddress\">\n" +
            "  <xsd:sequence>\n" +
            "   <xsd:element name=\"name\"   type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"street\" type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"city\"   type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"state\"  type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"zip\"    type=\"xsd:integer\"/>\n" +
            "  </xsd:sequence>\n" +
            "  <xsd:attribute name=\"country\" type=\"xsd:NMTOKEN\" fixed=\"US\"/>\n" +
            " </xsd:complexType>\n" +
            "</xsd:schema>";
    public static final String DEFAULT_XSD_UPDATED = "<xsd:schema xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"\n" +
            "           xmlns:tns=\"http://tempuri.org/PurchaseOrderSchema.xsd\"\n" +
            "           targetNamespace=\"http://tempuri.org/PurchaseOrderSchema.xsd\"\n" +
            "           elementFormDefault=\"qualified\">\n" +
            " <xsd:element name=\"PurchaseOrder\" type=\"tns:PurchaseOrderType\"/>\n" +
            " <xsd:complexType name=\"PurchaseOrderType\">\n" +
            "  <xsd:sequence>\n" +
            "   <xsd:element name=\"ShipTo\" type=\"tns:USAddress\" maxOccurs=\"3\"/>\n" +
            "   <xsd:element name=\"BillTo\" type=\"tns:USAddress\"/>\n" +
            "  </xsd:sequence>\n" +
            "  <xsd:attribute name=\"OrderDate\" type=\"xsd:date\"/>\n" +
            " </xsd:complexType>\n" +
            "\n" +
            " <xsd:complexType name=\"USAddress\">\n" +
            "  <xsd:sequence>\n" +
            "   <xsd:element name=\"name\"    type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"surname\" type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"street\"  type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"city\"    type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"state\"   type=\"xsd:string\"/>\n" +
            "   <xsd:element name=\"zip\"     type=\"xsd:integer\"/>\n" +
            "  </xsd:sequence>\n" +
            "  <xsd:attribute name=\"country\" type=\"xsd:NMTOKEN\" fixed=\"US\"/>\n" +
            " </xsd:complexType>\n" +
            "</xsd:schema>";

    public static final String DEFAULT_XML = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<shiporder orderid=\"889923\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:noNamespaceSchemaLocation=\"shiporder.xsd\">\n" +
            "  <orderperson>John Smith</orderperson>\n" +
            "  <shipto>\n" +
            "    <name>Ola Nordmann</name>\n" +
            "    <address>Langgt 23</address>\n" +
            "    <city>4000 Stavanger</city>\n" +
            "    <country>Norway</country>\n" +
            "  </shipto>\n" +
            "  <item>\n" +
            "    <title>Empire Burlesque</title>\n" +
            "    <note>Special Edition</note>\n" +
            "    <quantity>1</quantity>\n" +
            "    <price>10.90</price>\n" +
            "  </item>\n" +
            "  <item>\n" +
            "    <title>Hide your heart</title>\n" +
            "    <quantity>1</quantity>\n" +
            "    <price>9.90</price>\n" +
            "  </item>\n" +
            "</shiporder>";
    public static final String DEFAULT_XML_UPDATED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<shiporder orderid=\"889924\"\n" +
            "xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n" +
            "xsi:noNamespaceSchemaLocation=\"shiporder.xsd\">\n" +
            "  <orderperson>John Smith The Second</orderperson>\n" +
            "  <shipto>\n" +
            "    <name>Ola Nordmann</name>\n" +
            "    <address>Langgt 23</address>\n" +
            "    <city>4000 Stavanger</city>\n" +
            "    <country>Norway</country>\n" +
            "  </shipto>\n" +
            "  <item>\n" +
            "    <title>Empire Burlesque</title>\n" +
            "    <note>Special Edition</note>\n" +
            "    <quantity>1</quantity>\n" +
            "    <price>11.50</price>\n" +
            "  </item>\n" +
            "  <item>\n" +
            "    <title>Hide your heart</title>\n" +
            "    <quantity>1</quantity>\n" +
            "    <price>10.50</price>\n" +
            "  </item>\n" +
            "</shiporder>";
}
