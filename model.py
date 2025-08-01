import pandas as pd
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import accuracy_score
import joblib

# Load dataset
df = pd.read_csv('sp500_sample.csv')

# Feature engineering (simplified example)
df['Price_Change'] = df['Close'] - df['Open']
df['Target'] = (df['Close'].shift(-1) > df['Close']).astype(int)
df = df.dropna()

X = df[['Open', 'High', 'Low', 'Close', 'Volume', 'Price_Change']]
y = df['Target']

# Split and train
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
clf = RandomForestClassifier(n_estimators=100, random_state=42)
clf.fit(X_train, y_train)

# Save model
joblib.dump(clf, 'model.joblib')

# Evaluate
y_pred = clf.predict(X_test)
acc = accuracy_score(y_test, y_pred)
print(f'Model trained. Accuracy: {acc:.2f}')
