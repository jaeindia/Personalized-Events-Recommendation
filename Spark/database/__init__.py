from const import *

def load_db_table(sqlContext, table):
    url = 'jdbc:mysql://{}:{}/{}?user={}&password={}'.format(
            MYSQL_HOST,
            MYSQL_PORT,
            MYSQL_DB,
            MYSQL_USER,
            MYSQL_PASS
            )
    return sqlContext.read.format('jdbc').options(url=url, dbtable=table).load()
