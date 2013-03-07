
class CaseResult
{
    String result;
    boolean successfulResult;
    
    static Collection<String> successfulResults()
    {
        return CaseResult.findAllWhere(successfulResult:true)
    }
}
