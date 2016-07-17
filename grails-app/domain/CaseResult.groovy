
class CaseResult
{
    String result;
    boolean successfulResult; // For client or ILCBC.
    boolean statusWasAchieved; // An actual immigration status was successfully achieved.

    static Collection<String> successfulResults()
    {
        return findAllWhere(successfulResult:true)
    }

    static Collection<String> statusWasAchieved()
    {
        return findAllWhere(statusWasAchieved:true)
    }
}
