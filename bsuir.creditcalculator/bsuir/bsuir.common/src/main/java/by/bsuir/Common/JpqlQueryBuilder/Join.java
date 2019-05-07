package by.bsuir.Common.JpqlQueryBuilder;

class Join {
    private final JoinType _joinType;
    private final String _entity;


    public Join(JoinType joinType, String entity) {
        _joinType = joinType;
        _entity = entity;
    }


    public String stringify() {
        String joinString = getJoinString(_joinType);

        return String.format("%s JOIN %s", joinString, _entity);
    }


    private static String getJoinString(JoinType joinType) {
        switch (joinType) {
            case LEFT:
                return "LEFT";
            case INNER:
                return "INNER";
            case OUTER:
                return "OUTER";
            default:
                return "";
        }
    }
}
