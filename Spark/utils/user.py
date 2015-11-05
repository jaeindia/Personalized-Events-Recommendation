import numpy as np

def get_user_numeric_vector(user):
    gender = 2
    if user.gender == 'female':
        gender = 0
    else:
        gender = 1
    return np.array([
        (user.birthyear - 1992) / 2.0,
        user.timezone / 400.0,
        gender
    ])
