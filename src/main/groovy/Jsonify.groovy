import groovy.json.JsonOutput

class Jsonify {

    static void main(String[] args) {
        Client.list().each{
            JsonOutput.toJson($it)
        }
    }
}
