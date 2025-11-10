import sys
from PySide6.QtWidgets import (
    QApplication, QWidget, QLineEdit,
    QLabel, QPushButton, QVBoxLayout,
    QTextEdit, QMessageBox
)


class MockDatabase:
    """Simulates saving data to a database."""

    def save_profile(self, username, email, password, bio):
        """Prints the saved data to the console."""
        print("--- PROFILE SAVED TO MOCK DATABASE ---")
        print(f"Username: {username}")
        print(f"Email: {email}")
        print(f"Password: {password}")
        print(f"Biography: {bio}")
        print("--------------------------------------")



class UserProfile(QWidget):
    def __init__(self, db):
        super().__init__()
        self.db = db
        self.setWindowTitle("Accessible User Profile Form")
        self.setup_ui()

    def setup_ui(self):

        self.label_name = QLabel("&Username:")
        self.input_name = QLineEdit()
        self.input_name.setPlaceholderText("E.g., emicuciu")

        self.label_email = QLabel("&E-mail:")
        self.input_email = QLineEdit()
        self.input_email.setPlaceholderText("E.g., emicuciu@example.com")

        self.label_password = QLabel("New &Password:")
        self.input_password = QLineEdit()
        self.input_password.setEchoMode(QLineEdit.Password)

        self.label_bio = QLabel("&Biography:")
        self.input_bio = QTextEdit()
        self.input_bio.setPlaceholderText("Write a few words about yourself...")

        self.save_button = QPushButton("&Save Profile")

        # Accessibility API (setBuddy)
        # Here we add multiple 'buddy' relationships

        self.label_name.setBuddy(self.input_name)

        self.label_email.setBuddy(self.input_email)

        self.label_password.setBuddy(self.input_password)

        self.label_bio.setBuddy(self.input_bio)

        # Note: The '&' in the text (e.g., "&Username") creates
        # a keyboard shortcut (Alt+U) that automatically
        # moves focus to the buddy widget.

        layout = QVBoxLayout()
        layout.addWidget(self.label_name)
        layout.addWidget(self.input_name)
        layout.addWidget(self.label_email)
        layout.addWidget(self.input_email)
        layout.addWidget(self.label_password)
        layout.addWidget(self.input_password)
        layout.addWidget(self.label_bio)
        layout.addWidget(self.input_bio)
        layout.addWidget(self.save_button)

        self.setLayout(layout)

        self.save_button.clicked.connect(self.on_save)

    def on_save(self):
        """Collects the data and "saves" it."""
        username = self.input_name.text()
        email = self.input_email.text()
        password = self.input_password.text()
        bio = self.input_bio.toPlainText()

        if not username or not email or not password:
            QMessageBox.warning(self, "Error", "Username, E-mail, and Password fields are required!")
            return

        self.db.save_profile(username, email, password, bio)

        QMessageBox.information(self, "Success", "Profile saved successfully!")
        self.close()


if __name__ == "__main__":
    app = QApplication(sys.argv)

    database = MockDatabase()
    window = UserProfile(db=database)
    window.show()

    sys.exit(app.exec())