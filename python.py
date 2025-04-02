import pymssql
from tabulate import tabulate
import sys
import io
import configparser

sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

config = configparser.ConfigParser()
config.read('config.ini', encoding='utf-8')
db_server = config['Database']['server']
db_user = config['Database']['user']
db_password = config['Database']['password']
db_name = config['Database']['database']
db_charset = config['Database'].get('charset', 'cp1250')

with pymssql.connect(
    server=db_server, 
    user=db_user, 
    password=db_password,
    database=db_name, 
    charset=db_charset
    ) as polaczenie:

    with polaczenie.cursor() as kursor:
        sql = "SELECT * FROM Pracownicy"
        kursor.execute(sql)
        rows = kursor.fetchall()
        if rows:
            nazwy_kolumn = [opis[0] for opis in kursor.description]
            print(tabulate(rows, headers=nazwy_kolumn, tablefmt="grid"))
        else:
            print("Tabela jest pusta.")
