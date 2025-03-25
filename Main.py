import pymssql
from tabulate import tabulate
import sys
import io

# Принудительная настройка кодировки вывода
sys.stdout = io.TextIOWrapper(sys.stdout.buffer, encoding='utf-8')

# Данные для подключения
server = "mssql-2017.labs.wmi.amu.edu.pl"
user = "dbad_s498793"
password = "bWyQrhMOc3"
database = "dbad_s498793"

try:
    # Подключение с кодировкой cp1250
    conn = pymssql.connect(
        server, user, password, database,
        charset='cp1250'  # Экспериментируйте с cp1250/iso-8859-2
    )
    cursor = conn.cursor()

    cursor.execute("SELECT * FROM Pracownicy")
    rows = cursor.fetchall()

    if cursor.description:
        column_names = [col[0] for col in cursor.description]
        # Убедитесь, что tabulate не портит кодировку
        print(tabulate(rows, headers=column_names, tablefmt="grid"))
    else:
        print("Таблица пуста.")

    conn.close()

except Exception as e:
    print(f"Ошибка: {e}")