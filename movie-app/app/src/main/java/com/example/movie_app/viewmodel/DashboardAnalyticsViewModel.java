package com.example.movie_app.viewmodel;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.movie_app.models.AnalyticsDashboardResponse;
import com.example.movie_app.models.ChartDataPoint;
import com.example.movie_app.repository.AnalyticsRepository;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardAnalyticsViewModel extends AndroidViewModel {

    private final AnalyticsRepository repository;
    private final MutableLiveData<Integer> selectedFilterDays = new MutableLiveData<>(30);
    private final LiveData<AnalyticsDashboardResponse> analyticsData;
    private final MutableLiveData<String> operationMessage = new MutableLiveData<>();

    private final MutableLiveData<List<ChartDataPoint>> userRegistrationChart = new MutableLiveData<>();
    private final MutableLiveData<Integer> totalNewUsersCount = new MutableLiveData<>(0);

    private final FirebaseFirestore firestore;

    public DashboardAnalyticsViewModel(@NonNull Application application) {
        super(application);
        repository = new AnalyticsRepository();
        firestore = FirebaseFirestore.getInstance();

        analyticsData = Transformations.switchMap(selectedFilterDays, days -> {
            fetchUserRegistrationsFromFirestore(days);
            return repository.getAnalyticsData(days);
        });
    }

    public LiveData<AnalyticsDashboardResponse> getAnalyticsData() { return analyticsData; }
    public LiveData<Integer> getSelectedFilterDays() { return selectedFilterDays; }
    public LiveData<String> getOperationMessage() { return operationMessage; }

    public LiveData<List<ChartDataPoint>> getUserRegistrationChart() { return userRegistrationChart; }
    public LiveData<Integer> getTotalNewUsersCount() { return totalNewUsersCount; }

    public void setFilterDays(int days) {
        selectedFilterDays.setValue(days);
    }

    private Date getSafeDate(QueryDocumentSnapshot doc, String fieldName) {
        Object value = doc.get(fieldName);
        if (value == null) return null;

        if (value instanceof com.google.firebase.Timestamp) {
            return ((com.google.firebase.Timestamp) value).toDate();
        } else if (value instanceof java.util.Date) {
            return (java.util.Date) value;
        } else if (value instanceof Long) {
            return new Date((Long) value);
        } else if (value instanceof Double) {
            return new Date(((Double) value).longValue());
        } else if (value instanceof String) {
            String strVal = (String) value;
            try {
                return new Date(Long.parseLong(strVal));
            } catch (NumberFormatException e) {
                String[] formats = {"yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd", "dd/MM/yyyy"};
                for (String format : formats) {
                    try {
                        return new SimpleDateFormat(format, Locale.getDefault()).parse(strVal);
                    } catch (Exception ignored) {}
                }
            }
        }
        return null;
    }

    private void fetchUserRegistrationsFromFirestore(int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -days);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        Date startDate = calendar.getTime();

        firestore.collection("users")
                .whereGreaterThanOrEqualTo("createdAt", startDate)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null) {
                        List<Date> registrationDates = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Date date = getSafeDate(doc, "createdAt");
                            if (date != null) {
                                registrationDates.add(date);
                            }
                        }
                        processUserChartData(registrationDates, days);
                    } else {
                        userRegistrationChart.postValue(new ArrayList<>());
                    }
                });
    }

    private void processUserChartData(List<Date> dates, int days) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM", Locale.getDefault());
        Map<String, Integer> dayCountMap = new HashMap<>();
        List<String> orderedLabels = new ArrayList<>();

        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DAY_OF_YEAR, -(days - 1));

        for (int i = 0; i < days; i++) {
            String label = sdf.format(cal.getTime());
            orderedLabels.add(label);
            dayCountMap.put(label, 0);
            cal.add(Calendar.DAY_OF_YEAR, 1);
        }

        for (Date d : dates) {
            if (d != null) {
                String label = sdf.format(d);
                if (dayCountMap.containsKey(label)) {
                    dayCountMap.put(label, dayCountMap.get(label) + 1);
                }
            }
        }

        totalNewUsersCount.postValue(dates.size());

        int maxCount = 0;
        for (int count : dayCountMap.values()) {
            if (count > maxCount) maxCount = count;
        }

        List<ChartDataPoint> points = new ArrayList<>();
        for (String label : orderedLabels) {
            int count = dayCountMap.get(label);
            float heightInDp = maxCount > 0 ? ((float) count / maxCount) * 120.0f + 10.0f : 10.0f;

            ChartDataPoint point = new ChartDataPoint();
            point.setLabelDate(label);
            point.setHeightInDp((int) heightInDp);
            point.setHighlight(count == maxCount && maxCount > 0);
            points.add(point);
        }

        userRegistrationChart.postValue(points);
    }

    public void loadAnalytics() {
        Integer current = selectedFilterDays.getValue();
        selectedFilterDays.setValue(current != null ? current : 30);
    }

    public void exportReport() {}
}