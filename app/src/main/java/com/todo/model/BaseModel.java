package com.todo.model;

import android.util.Log;

import com.todo.common.Utils;

import java.util.ArrayList;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmObject;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

public class BaseModel<T extends RealmObject> {

    private static volatile BaseModel _instance = null;

    public static BaseModel Instance() {
        if (_instance == null) {
            synchronized (BaseModel.class) {
                _instance = new BaseModel();
            }
        }
        return _instance;
    }

    public ArrayList<T> getAll(Class<T> m_type) {
        ArrayList<T> m_modelList = new ArrayList<>();
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type);
            RealmResults<T> results = query.findAll();
            m_modelList = new ArrayList<>(results);
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return m_modelList;
    }

    public T getFirst(Class<T> m_type) {
        T info = null;
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type);
            info = query.findFirst();
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return info;
    }

    public ArrayList<T> getAllContainedValue(Class<T> m_type, String key, String value) {
        ArrayList<T> m_modelList = new ArrayList<>();
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).contains(key, value, Case.INSENSITIVE);
            RealmResults<T> results = query.findAll();
            if (results != null && results.size() > 0) {
                m_modelList = new ArrayList<>(results);
            } else
                m_modelList = null;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return m_modelList;
    }

    public void ClearDB(Class<T> type) {
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            RealmQuery<T> query = realm.where(type);
            RealmResults<T> results = query.findAll();
            results.deleteAllFromRealm();
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Utils.LogException(e);
        }
    }

    public ArrayList<T> getAllObjectById(Class<T> m_type, String key, boolean value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            RealmResults<T> results = query.findAll();
            if (results != null && results.size() > 0)
                return new ArrayList<>(results);
            else
                return new ArrayList<>();

        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T getObjectById(Class<T> m_type, String key, String value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            T results = query.findFirst();
            return results;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T getObjectById(Class<T> m_type, String key, long value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            T results = query.findFirst();
            return results;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public ArrayList<T> getAllObjectById(Class<T> m_type, String key, String value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            RealmResults<T> results = query.findAll();
            if (results != null && results.size() > 0)
                return new ArrayList<>(results);
            else
                return new ArrayList<>();

        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public ArrayList<T> getAllObjectById(Class<T> m_type, String key, int value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            RealmResults<T> results = query.findAll();
            if (results != null && results.size() > 0)
                return new ArrayList<>(results);
            else
                return new ArrayList<>();

        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public ArrayList<T> getAllObjectByIdWithSort(Class<T> m_type, String key, String value, String sortKey, Sort order) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            RealmResults<T> results = query.findAll().sort(sortKey, order);
            if (results != null && results.size() > 0)
                return new ArrayList<>(results);
            else
                return new ArrayList<>();

        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public ArrayList<T> getAllObjectWithSort(Class<T> m_type, String sortKey, Sort order) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type);
            RealmResults<T> results = query.findAll().sort(sortKey, order);
            if (results != null && results.size() > 0)
                return new ArrayList<>(results);
            else
                return new ArrayList<>();

        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T getObjectById(Class<T> m_type, String key, int value) {
        try {
            int i = 10;
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            T results = query.findFirst();
            return results;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T deleteObjectById(Class<T> m_type, String key, int value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            T results = query.findFirst();
            realm.beginTransaction();
            results.deleteFromRealm();
            realm.commitTransaction();
            realm.close();
            return results;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T deleteObjectById(Class<T> m_type, String key, String value) {
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmQuery<T> query = realm.where(m_type).equalTo(key, value);
            T results = query.findFirst();
            realm.beginTransaction();
            results.deleteFromRealm();
            realm.commitTransaction();
            realm.close();
            return results;
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return null;
    }

    public T addObject(Class<T> m_type, T obj) {
        T result = null;
        try {
            Realm realm = Realm.getDefaultInstance();
            realm.beginTransaction();
            result = realm.copyToRealmOrUpdate(obj);
            realm.commitTransaction();
            realm.close();
        } catch (Exception e) {
            Utils.LogException(e);
        }
        return result;
    }

    public int autoIncrementID(final Class<T> m_type, final String id){
        int nextId;
        Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        Number currentIdNum = realm.where(m_type).max(id);
        if(currentIdNum == null) {
            nextId=1;
        } else {
            nextId= currentIdNum.intValue() + 1;
        }
        Log.e("1", String.valueOf(nextId));
        realm.commitTransaction();
        return nextId;
    }

}
