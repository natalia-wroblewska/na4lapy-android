package pl.kodujdlapolski.na4lapy.ui.about_shelter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.kodujdlapolski.na4lapy.R;
import pl.kodujdlapolski.na4lapy.model.Shelter;
import pl.kodujdlapolski.na4lapy.presenter.AboutShelterPresenter;

/**
 * Created by Natalia on 2016-03-01.
 */
public class AboutShelterFragment extends Fragment {

    private AboutShelterPresenter presenter;
    private ActionBar actionBar;

    @Bind(R.id.shelter_account_label)
    TextView shelterAccountLabel;
    @Bind(R.id.shelter_account_number)
    TextView shelterAccountNumber;

    @Bind(R.id.shelter_address_label)
    TextView shelterAddressLabel;
    @Bind(R.id.shelter_address)
    TextView shelterAddress;

    @Bind(R.id.shelter_email_label)
    TextView shelterEmailLabel;
    @Bind(R.id.shelter_email_address)
    TextView shelterEmailAddress;

    @Bind(R.id.shelter_phone_label)
    TextView shelterPhoneLabel;
    @Bind(R.id.shelter_phone_number)
    TextView shelterPhoneNumber;

    @Bind(R.id.shelter_www_label)
    TextView shelterWwwLabel;
    @Bind(R.id.shelter_www)
    TextView shelterWWW;


    @Bind(R.id.about_shelter_progress)
    ProgressBar progressBar;
    @Bind(R.id.about_shelter_content)
    ScrollView aboutShelterContent;

    @Bind(R.id.error_container)
    LinearLayout errorContainer;

    @SuppressWarnings("unused")
    @OnClick(R.id.try_again_btn)
    void onTryAgainClick() {
        errorContainer.setVisibility(View.GONE);
        presenter.startDownloadingData();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about_shelter, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter = new AboutShelterPresenter(this);
    }

    public void populateView(Shelter shelter) {
        actionBar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle(String.format(getString(R.string.about_shelter_page_title), shelter.getName()));
        }
        shelterAccountNumber.setText(getFormattedAccount(shelter.getAccountNumber()));
        shelterWWW.setText(shelter.getWebsite());
        shelterEmailAddress.setText(shelter.getEmail());
        shelterPhoneNumber.setText(shelter.getPhoneNumber());
        shelterAddress.setText(String.format(getString(R.string.shelter_address_template),
                shelter.getStreet(),
                shelter.getBuildingNumber(),
                shelter.getPostalCode(),
                shelter.getCity()));
    }

    private String getFormattedAccount(String accountNumber) {
        String accountPart1 = accountNumber.substring(0, 2);
        String accountPart2 = accountNumber.substring(2);
        String shelterFormatted = splitStringEvery(accountPart2, 4);
        return accountPart1 + " " + shelterFormatted;
    }

    private String splitStringEvery(String s, int interval) {
        StringBuilder result = new StringBuilder();
        int arrayLength = (int) Math.ceil(((s.length() / (double) interval)));
        int lastIndex = arrayLength - 1;

        int j = 0;
        for (int i = 0; i < lastIndex; i++) {
            result.append(s.substring(j, j + interval));
            result.append(" ");
            j += interval;
        }
        result.append(s.substring(j));
        return result.toString();
    }

    public void showProgressHideContent(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        aboutShelterContent.setVisibility(show ? View.GONE : View.VISIBLE);
    }

    public void showError() {
        progressBar.setVisibility(View.GONE);
        aboutShelterContent.setVisibility(View.GONE);
        errorContainer.setVisibility(View.VISIBLE);
    }


    public String getFormattedInfoText() {
        String result = "";

        result += (shelterAddressLabel.getText());
        result += ("\n");
        result += (shelterAddress.getText());
        result += ("\n\n");

        result += (shelterEmailLabel.getText());
        result += ("\n");
        result += (shelterEmailAddress.getText());
        result += ("\n\n");

        result += (shelterPhoneLabel.getText());
        result += ("\n");
        result += (shelterPhoneNumber.getText());
        result += ("\n\n");

        result += (shelterWwwLabel.getText());
        result += ("\n");
        result += (shelterWWW.getText());
        result += ("\n\n");

        result += (shelterAccountLabel.getText());
        result += ("\n");
        result += (shelterAccountNumber.getText());

        return result;
    }

    public String getFormattedTitle() {
        if (actionBar != null && actionBar.getTitle() != null) {
            return actionBar.getTitle().toString();
        } else return "";
    }

    public void onActivityStart() {
        if (presenter != null) {
            presenter.onActivityStart();
        }
    }

    public void onActivityStop() {
        if (presenter != null) {
            presenter.onActivityStop();
        }
    }
}